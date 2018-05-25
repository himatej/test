import Common

freeStyleJob('datapathJob') {

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
        shell('echo "Running datapath job"')
    }
}