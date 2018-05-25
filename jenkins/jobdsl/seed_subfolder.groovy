freeStyleJob('seed_subfolder'){
    parameters{
        stringParam('BRANCH_TO_GENERATE', 'master', '')
    }

    environmentVariables{
        env('binding_branch', '${BRANCH_TO_GENERATE}')
    }

    steps{
        dsl{
            text("folder(binding.variables.get('binding_branch'))")
        }
        dsl{
            lookupStrategy('SEED_JOB')
            external('jenkins/jobdsl/seed.groovy')
        }

        downstreamParameterized{
            trigger('${BRANCH_TO_BUILD}/seed')
        }
    }
}