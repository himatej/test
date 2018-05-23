freeStyleJob('BuildJob2') {

    properties {
        githubProjectUrl(Common.githubProjectURL)
    }

    scm {
        git {
            remote {
                credentials(Common.githubCredentialsID)
                github(Common.githubURL)
                refspec('+refs/pull/*:refs/remotes/origin/pr/*')
            }
            extensions{
                cloneOptions{

                }
            }
            branch('${sha1}')
        }
    }

    steps {
        shell('echo "Running build job 2"')
    }
}