import util.*

class SeedJob extends BaseJob{
    def branchEnv

    SeedJob(binding) {
        super(binding, 'freestyle', binding.variables.get(SeedSubFolderJob.branchParamKey) + '/seed')
    }

    def generate() {
        super.generate()
        this.generateParams()
        this.generateSteps()
        return this
    }

    protected generateGit() {
        this.gitScm.with {
            gitBranch = '${' + branchParamKey + '}'
        }
        super.generateGit()
    }

    private generateParams() {
        this.jobDsl.parameters {
            stringParam(branchParamKey, this.branchEnv, '')
        }
    }

    private generateSteps() {
        this.jobDsl.steps {
            dsl {
                external('jenkins/jobdsl/load_jobdsl.groovy')
                lookupStrategy('SEED_JOB')
            }
        }
    }
}