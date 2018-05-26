import util.*

class SeedSubFolderJob extends BaseJob{
    def static branchParamKey = 'BRANCH_TO_BUILD'

    SeedSubFolderJob(binding) {
        super(binding, 'freestyle', 'seed_subfolder')
    }

    def generate() {
        this.generateParams()
        this.generateSteps()
        return this
    }

    protected generateGit() {
        this.gitScm.with {
            gitBranch = '${' + branchParamKey + '}'
        }
        super.generateGit()
    }

    private generateParams() {
        this.jobDsl.parameters {
            stringParam(branchParamKey, 'master', '')
        }
    }

    private generateSteps() {
        this.jobDsl.steps {
            dsl {
                text("folder(binding.variables.get('" + branchParamKey + "'))")
                lookupStrategy('SEED_JOB')
            }
            dsl {
                external('jenkins/jobdsl/seed.groovy')
                lookupStrategy('SEED_JOB')
            }

            //trigger newly generated seed job
            downstreamParameterized {
                trigger('${' + branchParamKey + '}/seed') {
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
