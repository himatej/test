import Common
freeStyleJob('master/BuildJob1') {

    parameters {
        stringParam('BRANCH_TO_BUILD', 'master', '')
    }

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
            branch('refs/remotes/origin/${BRANCH_TO_BUILD}')
        }
    }

    steps {
        shell('echo "Running build job 1"')
    }
}