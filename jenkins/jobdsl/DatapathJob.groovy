import util.*

class DatapathJob {
    def jobDsl
    def gitScm
    def branchParamKey = 'Branch'
    def defaultBranchValue

    DatapathJob(binding) {
        this.defaultBranchValue = binding.variables.get(SeedJob.branchParamKey)
        this.jobDsl = BaseJob.getFreeStyleJob(binding.jobFactory, 'Datapath')
    }

    def generate() {
        this.generateGit()
        this.generateParams()
        this.generateSteps()
        return this
    }

    private generateGit() {
        this.gitScm = new GitScm()
        this.gitScm.with {
            gitBranch = 'refs/remotes/origin/${' + this.branchParamKey + '}'
        }
        this.gitScm.generate(this.jobDsl)
    }

    private generateParams() {
        this.jobDsl.parameters {
            stringParam(this.branchParamKey, this.defaultBranchValue, '')
        }
    }

    private generateSteps() {
        this.jobDsl.steps {
            shell('echo "Running datapath job"')
        }
    }
}