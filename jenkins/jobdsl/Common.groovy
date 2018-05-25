class Common{
    final static githubUser = 'himatej'
    final static githubURL = 'himatej/test'
    final static githubProjectURL = 'https://github.com/himatej/test'

    //id comes from jenkins store
    final static githubCredentialsID = '93c6a674-4dbd-47de-b9af-aabf9462c183'

    //add the folders to check for changes and mapping respective job script
    final static foldersToCheck = [
            controller : 'controllerJob',
            datapath : 'datapathJob'
    ]
}