import util.*

class SeedJob {
    def jobDsl
    def static branchParamKey = 'Branch'
    def branchParamValue
    def branchEnv

    SeedJob(binding) {
        this.branchEnv = binding.variables.get(SeedSubFolderJob.branchParamKey)
        this.jobDsl = BaseJob.getFreeStyleJob(binding.jobFactory, this.branchEnv + '/seed')
    }

    def generate() {
        this.generateParams()
        this.generateSteps()
        return this
    }

    private generateParams() {
        this.jobDsl.parameters {
            stringParam(this.branchParamKey, this.branchEnv, '')
        }
    }

    private generateSteps(){
        this.jobDsl.steps{
            dsl{
                text('new ControllerJob(binding).generate()')
                lookupStrategy('SEED_JOB')
            }
        }
    }
}