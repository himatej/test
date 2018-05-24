import Common

//TODO change master to take branch name and create folder
//TODO write wrapper class for freeStyleJob
multiJob('master/ghprbhook') {

    properties {
        githubProjectUrl(Common.githubProjectURL)
    }

    scm {
        git {
            remote {
                credentials(Common.githubCredentialsID)
                github(Common.githubURL)
                refspec('+refs/pull/*:refs/remotes/origin/pr/*')
            }
            extensions {
                cloneOptions {

                }
            }
            branch('${sha1}')
        }
    }

    triggers {
        //TODO disable this webhook for private folders
        githubPullRequest {
            admin(Common.githubUser)
            triggerPhrase('test this please')
            useGitHubHooks()
            permitAll()
            displayBuildErrorsOnDownstreamBuilds()
        }
    }

    steps {
        //fix for new changes to master not detecting
        shell("sudo git fetch")

        //Loop through all folders to check for changes and run respective jobs
        for (folder in Common.foldersToCheck.keySet()) {
            conditionalSteps {
                condition {
                    shell("\${WORKSPACE}/jenkins/shell/checkchanges.sh ${folder}")
                }
                steps {
                    phase(${folder}){
                        phaseJob(Common.foldersToCheck.get(folder)){
                            currentJobParameters(false)
                            parameters{
                                //$GIT_BRANCH is set by jenkins when ghprbhook is triggered
                                predefinedProp('BRANCH_TO_BUILD', '$GIT_BRANCH')
                            }
                        }
                    }
                }
            }
        }
    }


    publishers {
        //using dynamic DSL
        //TODO write wrapper for this for future reusability and change option values to variables
        s3BucketPublisher {
            //DO NOT CHANGE 'aws-jenkins'
            //given in system configuration
            //TODO make 'aws-jenkins' dynamic(directly taken from system configuration if possible)
            profileName('aws-jenkins')
            entries {
                entry {
                    dontWaitForConcurrentBuildCompletion(false)
                    consoleLogLevel('INFO')
                    pluginFailureResultConstraint('FAILURE')
                    excludedFile('')
                    gzipFiles(false)
                    keepForever(true)
                    showDirectlyInBrowser(false)
                    bucket('jenkins-valtix/${JOB_NAME}-${BUILD_NUMBER}')
                    sourceFile('controller/controller.txt')
                    selectedRegion('us-east-1')
                    storageClass('STANDARD')
                    noUploadOnFailure(true)
                    uploadFromSlave(true)
                    managedArtifacts(false)
                    useServerSideEncryption(false)
                    flatten(false)
                }
            }
            userMetadata {

            }
        }
    }
}