def build_url = binding.variables.get('BUILD_URL')
freeStyleJob('seed_subfolder') {
    parameters {
        stringParam('BRANCH_TO_GENERATE', 'master', '')
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
//No subfolder in master folders
    if (!build_url.contains('master')) {
        steps {
            dsl {
                lookupStrategy('SEED_JOB')
                text("folder(binding.variables.get('BRANCH_TO_GENERATE'))")
            }
            dsl {
                lookupStrategy('SEED_JOB')
                external('jenkins/jobdsl/seed.groovy')
            }

            downstreamParameterized {
                trigger('${BRANCH_TO_GENERATE}/seed') {
                    block {
                        buildStepFailure('FAILURE')
                        failure('FAILURE')
                        unstable('UNSTABLE')
                    }
                }
            }
        }
    }
}