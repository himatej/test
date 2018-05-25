freeStyleJob(binding.variables.get('binding_branch')+'/seed'){
    steps{
        dsl{
            lookupStrategy('SEED_JOB')
            external('jenkins/jobdsl/*Job.groovy')
        }
    }
}