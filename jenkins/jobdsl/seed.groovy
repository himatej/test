import Common

//comes from seed_subfolder
//FIXME make the independent of the seed_subfolder
def branch_folder = binding.variables.get('BRANCH_TO_GENERATE')
freeStyleJob(branch_folder+'/seed'){
    scm {
        git {
            remote {
                credentials(Common.githubCredentialsID)
                github(Common.githubURL)
            }

            //TODO parameterize branch name
            branch('refs/remotes/origin/'+branch_folder)
        }
    }
    steps{
        dsl{
            lookupStrategy('SEED_JOB')
            external('jenkins/jobdsl/seed_subfolder.groovy')
            external('jenkins/jobdsl/*Job.groovy')
        }
    }
}