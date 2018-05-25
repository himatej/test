freeStyleJob('seed_subfolder'){
    parameters{
        stringParameter('BRANCH_TO_GENERATE', 'master', '')
    }

    environmentVariables{
        env('binding_branch', 'BRANCH_TO_GENERATE')
    }

    steps{
        dsl{
            lookupStrategy('SEED_JOB')
            text("folder(binding.variables.get('binding_branch'))")
            external('jenkins/jobdsl/seed.groovy')
        }

        downstreamParameterized{
            trigger('${BRANCH_TO_BUILD}/seed')
        }
    }
}