import Common
freeStyleJob('master/BuildJob2') {

    properties {
        githubProjectUrl(Common.githubProjectURL)
    }

    scm {
        git {
            remote {
                credentials(Common.githubCredentialsID)
                github(Common.githubURL)
            }

            //TODO parameterize branch name
            branch('refs/remotes/origin/master')
        }
    }

    steps {
        shell('echo "Running build job 2"')
    }
}