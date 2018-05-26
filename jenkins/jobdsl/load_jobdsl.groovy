new ControllerJob(binding).generate()
new DatapathJob(binding).generate()
new GhPrbHook(binding).generate()

//do not generate seed subfolder for master
if(!binding.variables.get('BUILD_URL').contains('master')){
    new SeedSubFolderJob(binding).generate()
}
