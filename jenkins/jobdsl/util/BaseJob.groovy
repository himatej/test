package util
class BaseJob{
    def static getFreeStyleJob(dslFactory, jobName){
        return dslFactory.freeStyleJob(jobName)
    }

    def static getMultiJob(dslFactory, jobName){
        return dslFactory.multiJob(jobName)
    }
}
