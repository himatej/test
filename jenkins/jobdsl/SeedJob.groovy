import util.*

class SeedJob {
    def jobDsl
    def static branchParamKey = 'Branch'
    def branchParamValue
    def branchEnv

    SeedJob(binding) {
        this.branchEnv = binding.variables.get(SeedSubFolder.branchParamKey)
        this.jobDsl = BaseJob.getFreeStyleJob(binding.jobFactory, this.branchEnv + '/seed')
    }

    def generate() {
        this.generateParams()
        return this
    }

    private generateParams() {
        this.jobDsl.parameters {
            stringParam(this.branchParamKey, this.branchParamValue, '')
        }
    }
}