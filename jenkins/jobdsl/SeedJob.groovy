import util.*

class SeedJob {
    def jobDsl
    def gitScm
    def static branchParamKey = 'Branch'
    def branchEnv

    SeedJob(binding) {
        this.branchEnv = binding.variables.get(SeedSubFolderJob.branchParamKey)
        this.jobDsl = BaseJob.getFreeStyleJob(binding.jobFactory, this.branchEnv + '/seed')
    }

    def generate() {
        this.generateParams()
        this.generateSteps()
        this.generateGit()
        return this
    }

    private generateGit() {
        this.gitScm = new GitScm()
        this.gitScm.with {
            gitBranch = '${' + branchParamKey + '}'
        }
        this.gitScm.generate(this.jobDsl)
    }

    private generateParams() {
        this.jobDsl.parameters {
            stringParam(this.branchParamKey, this.branchEnv, '')
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