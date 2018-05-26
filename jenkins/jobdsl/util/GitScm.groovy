package util

class GitScm {
    final static githubUser = 'himatej'
    final static githubURL = 'himatej/test'
    final static githubProjectURL = 'https://github.com/himatej/test'

    //id comes from jenkins store
    final static githubCredentialsID = '93c6a674-4dbd-47de-b9af-aabf9462c183'

    def gitBranch = 'master'
    def remoteRefSpec = ''
    def isClone = false

    GitScm() {
    }

    void generate(jobDsl) {
        jobDsl.scm {
            git {
                remote {
                    credentials(githubCredentialsID)
                    github(githubURL)
                    if (remoteRefSpec != '') {
                        refspec(remoteRefSpec)
                    }
                }
                extensions {
                    if (isClone) {
                        cloneOptions {

                        }
                    }
                }
                branch(gitBranch)
            }
        }
    }
}