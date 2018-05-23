import Common

final foldersToCheck = [
        build1 : 'BuildJob1',
        build2 : 'BuildJob2'
]

//TODO change master to take branch name and create folder
//TODO write wrapper class for freeStyleJob
freeStyleJob('master/ghmergehook') {

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
            extensions{
                cloneOptions{

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
        //Loop through all folders to check for changes and run respective jobs
        for (folder in foldersToCheck.keySet()){
            conditionalSteps {
                condition {
                    shell('${WORKSPACE}/jenkins/shell/CheckChanges.sh '+${folder})
                }
                steps {
                    downstreamParameterized {
                        trigger(foldersToCheck.get(folder)) {
                            parameters {
                                predefinedProp('BRANCH_TO_BUILD', '$GIT_BRANCH')
                            }
                            block {
                                buildStepFailure('FAILURE')
                                failure('FAILURE')
                                unstable('UNSTABLE')
                            }
                        }
                    }
                }
            }
        }
    }
}