freeStyleJob('seed_subfolder'){
    parameters{
        stringParam('BRANCH_TO_GENERATE', 'master', '')
    }

    environmentVariables{
        env('binding_branch', '${BRANCH_TO_GENERATE}')
    }

    scm {
        git {
            remote {
                credentials('93c6a674-4dbd-47de-b9af-aabf9462c183')
                github('himatej/test')
            }

            //TODO parameterize branch name
            branch('refs/remotes/origin/${BRANCH_TO_GENERATE}')
        }
    }

    steps{
        dsl{
            lookupStrategy('SEED_JOB')
            text("folder(binding.variables.get('binding_branch'))")
        }
        dsl{
            lookupStrategy('SEED_JOB')
            external('jenkins/jobdsl/seed.groovy')
        }

        downstreamParameterized{
            trigger('${BRANCH_TO_GENERATE}/seed'){
                block {
                    buildStepFailure('FAILURE')
                    failure('FAILURE')
                    unstable('UNSTABLE')
                }
            }
        }
    }
}