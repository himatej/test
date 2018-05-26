import util.*

class ControllerJob {
    def jobDsl
    def gitScm
    def branchParamKey = 'Branch'
    def defaultBranchValue

    ControllerJob(binding) {
        this.defaultBranchValue = binding.variables.get(SeedJob.branchParamKey)
        this.jobDsl = BaseJob.getFreeStyleJob(binding.jobFactory, 'Controller')
    }

    def generate() {
        this.generateGit()
        this.generateParams()
        this.generateSteps()
        return this
    }

    def generateGit() {
        this.gitScm = new GitScm()
        this.gitScm.with {
            gitBranch = 'refs/remotes/origin/${' + this.branchParamKey + '}'
        }
        this.gitScm.generate(this.jobDsl)
    }

    def generateParams() {
        this.jobDsl.parameters {
            stringParam(this.branchParamKey, this.defaultBranchValue, '')
        }
    }

    def generateSteps() {
        this.jobDsl.steps {
            shell('echo "Running controller job"')
        }
    }
}