package util
class BaseJob{
    def jobDsl
    def gitScm
    def dslEnvVars
    def currentPath
    def BUILD_URL
    static branchParamKey = 'Branch'

    def static getFreeStyleJob(dslFactory, jobName){
        return dslFactory.freeStyleJob(jobName)
    }

    def static getMultiJob(dslFactory, jobName){
        return dslFactory.multiJob(jobName)
    }

    BaseJob(binding, jobType, jobName){
        switch (jobType){
            case 'freestyle':
                this.jobDsl = getFreeStyleJob(binding.jobFactory, jobName)
                break
            case 'multi':
                this.jobDsl = getMultiJob(binding.jobFactory, jobName)
        }

        this.dslEnvVars = binding.variables

        this.BUILD_URL = dslEnvVars.get('BUILD_URL')
        this.currentPath = dslEnvVars.get('JOB_NAME').replace(binding.variables.get('JOB_BASE_NAME'), "")

        this.createGit()
    }

    protected generate(){
        this.generateGit()
        return this
    }

    private createGit(){
        this.gitScm = new GitScm()
    }

    protected generateGit(){
        this.gitScm.generate(this.jobDsl)
    }
}
