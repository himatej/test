import util.*

class SeedSubFolderJob {
    def jobDsl
    def gitScm
    def static branchParamKey = 'BRANCH_TO_BUILD'

    SeedSubFolderJob(binding) {
        this.jobDsl = BaseJob.getFreeStyleJob(binding.jobFactory, 'seed_subfolder')
    }

    def generate() {
        this.generateParams()
        this.generateSteps()
        this.generateGit()
        return this
    }

    private generateGit() {
        this.gitScm = new GitScm()
        this.gitScm.with {
            gitBranch = '${' + branchParamKey + '}'
        }
        this.gitScm.generate(this.jobDsl)
    }

    private generateParams() {
        this.jobDsl.parameters {
            stringParam(this.branchParamKey, 'master', '')
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
        }
    }
}
