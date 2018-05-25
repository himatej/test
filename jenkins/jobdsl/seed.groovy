import Common
freeStyleJob(binding.variables.get('binding_branch')+'/seed'){
    scm {
        git {
            remote {
                credentials(Common.githubCredentialsID)
                github(Common.githubURL)
            }

            //TODO parameterize branch name
            branch('refs/remotes/origin/'+binding.variables.get('binding_branch'))
        }
    }
    steps{
        dsl{
            lookupStrategy('SEED_JOB')
            external('jenkins/jobdsl/*Job.groovy')
        }
    }
}