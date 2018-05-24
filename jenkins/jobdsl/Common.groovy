class Common{
    final static githubUser = 'himatej'
    final static githubURL = 'himatej/test'
    final static githubProjectURL = 'https://github.com/himatej/test'

    //id comes from jenkins store
    final static githubCredentialsID = '58dce1a2-9b50-4f78-b828-cb24fa8f1fe3'

    //add the folders to check for changes and mapping respective job script
    final static foldersToCheck = [
            controller : 'controllerJob',
            datapath : 'datapathJob'
    ]
}