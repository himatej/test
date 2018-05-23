final foldersToCheck = [
        build1 : 'BuildJob1',
        build2 : 'BuildJob2'
]

freeStyleJob('ghmergehook') {

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
                    shell("../shell/CheckChanges.sh ${folder}")
                }
                steps {
                    downstreamParameterized {
                        trigger(foldersToCheck.get(folder)) {
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