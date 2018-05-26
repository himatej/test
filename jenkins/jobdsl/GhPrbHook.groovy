import util.*

class GhPrbHook extends BaseJob{
    def branchParamValue = '${sha1}'
    def branchEnv

    final static foldersToCheck = [
            controller: 'Controller',
            datapath  : 'Datapath'
    ]

    GhPrbHook(binding) {
        super(binding, 'multi', 'ghprbhook')
        this.branchEnv = dslEnvVars.get(branchParamKey)
    }

    def generate() {
        super.generate()

        //dont trigger github merge hook for non master builds
        if (!BUILD_URL.contains('private')) {
            this.generateTriggers()
        } else {
            this.branchParamValue = this.branchEnv
        }

        this.generateSteps()

        this.generateParams()

        return this
    }

    protected generateGit() {
        this.gitScm.with {
            gitBranch = '${' + branchParamKey + '}'
            remoteRefSpec = '+refs/pull/*:refs/remotes/origin/pr/*'
            isClone = true
        }
        super.generateGit()
    }

    private generateTriggers() {
        this.jobDsl.triggers {
            githubPullRequest {
                admin(GitScm.githubUser)
                triggerPhrase('test this please')
                useGitHubHooks()
                permitAll()
                displayBuildErrorsOnDownstreamBuilds()
            }
        }
    }

    private generateSteps() {
        this.jobDsl.steps {
            //fix for new changes to master not detecting
            shell("sudo git fetch")

            //Loop through all folders to check for changes and run respective jobs
            for (folder in foldersToCheck.keySet()) {
                conditionalSteps {
                    condition {
                        shell("\${WORKSPACE}/jenkins/shell/checkchanges.sh ${folder}")
                    }
                    steps {
                        phase("${folder}") {
                            phaseJob(this.currentPath + foldersToCheck.get(folder)) {
                                currentJobParameters(false)
                                parameters {
                                    //$GIT_BRANCH is set by jenkins when ghprbhook is triggered
                                    predefinedProp('BRANCH_TO_BUILD', '$GIT_BRANCH')
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private generateParams() {
        this.jobDsl.parameters {
            stringParam(branchParamKey, this.branchParamValue, '')
        }
    }
}
