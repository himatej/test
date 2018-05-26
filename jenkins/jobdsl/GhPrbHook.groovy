import util.*

class GhPrbHook {
    def jobDsl
    def gitScm
    def BUILD_URL
    def branchParamKey = 'Branch'
    def branchParamValue = '${sha1}'
    def branchEnv
    def currentPath

    final static foldersToCheck = [
            controller: 'Controller',
            datapath  : 'Datapath'
    ]

    GhPrbHook(binding) {
        this.jobDsl = BaseJob.getMultiJob(binding.jobFactory, 'ghprbhook')
        this.BUILD_URL = binding.variables.get('BUILD_URL')
        this.branchEnv = binding.variables.get(SeedJob.branchParamKey)
        this.currentPath = binding.variables.get('JOB_NAME').replace(binding.variables.get('JOB_BASE_NAME'), "")
    }

    def generate() {

        this.generateGit()

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

    def generateGit() {
        this.gitScm = new GitScm()
        this.gitScm.with {
            gitBranch = '${' + branchParamKey + '}'
            remoteRefSpec = '+refs/pull/*:refs/remotes/origin/pr/*'
            isClone = true
        }
        this.gitScm.generate(this.jobDsl)
    }

    def generateTriggers() {
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

    def generateSteps() {
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
                            phaseJob(this.currentPath + '/' + foldersToCheck.get(folder)) {
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

    def generateParams() {
        this.jobDsl.parameters {
            stringParam(this.branchParamKey, this.branchParamValue, '')
        }
    }
}
