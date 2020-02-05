     /*def call(){
     def server = Artifactory.server 'Jfrog_artifactory'
      server.bypassProxy = true
      server.username = 'admin'
      server.password = 'password'
      def uploadSpec = """{
      "files": [
    {
      "pattern": "*.zip",
      "target": "target/"
    }
   ]
   }"""
    server.upload(uploadSpec)
    def buildInfo = server.upload uploadSpec
    server.publishBuildInfo buildInfo
    }*/
       
def call(Map conf){
       stage ('Artifactory configuration') {
            
                rtServer (
                    id: conf.artifactoryId
                )

                rtMavenDeployer (
                    id: "MAVEN_DEPLOYER",
                    serverId: conf.artifactoryId,
                    releaseRepo: "libs-release-local",
                    snapshotRepo: "libs-snapshot-local"
                )

                rtMavenResolver (
                    id: "MAVEN_RESOLVER",
                    serverId: conf.artifactoryId,
                    releaseRepo: "libs-release",
                    snapshotRepo: "libs-snapshot"
                )
            
        }

        stage ('Exec Maven') {
            
                rtMavenRun (
                    tool: conf.mavenId, // Tool name from Jenkins configuration
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: "MAVEN_DEPLOYER",
                    resolverId: "MAVEN_RESOLVER"
                )
            
        }

        stage ('Publish build info') {
            
                rtPublishBuildInfo (
                    serverId: conf.artifactoryId
                )
            }
        
}
