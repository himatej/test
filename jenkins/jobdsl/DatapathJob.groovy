import util.*

class DatapathJob extends BaseJob{
    def defaultBranchValue

    DatapathJob(binding) {
        super(binding, 'freestyle', 'Datapath')
        this.defaultBranchValue = this.dslEnvVars.get(branchParamKey)
    }

    def generate() {
        super.generate()
        this.generateParams()
        this.generateSteps()
        return this
    }

    protected generateGit() {
        this.gitScm.with {
            gitBranch = 'refs/remotes/origin/${' + branchParamKey + '}'
        }
        super.generateGit()
    }

    private generateParams() {
        this.jobDsl.parameters {
            stringParam(branchParamKey, this.defaultBranchValue, '')
        }
    }

    private generateSteps() {
        this.jobDsl.steps {
            shell('echo "Running datapath job"')
        }
    }
}