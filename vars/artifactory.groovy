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
       
def call(){
       stage ('Artifactory configuration') {
            
                rtServer (
                    id: "Jfrog_artifactory"
                )

                rtMavenDeployer (
                    id: "MAVEN_DEPLOYER",
                    serverId: "Jfrog_artifactory",
                    releaseRepo: "libs-release-local",
                    snapshotRepo: "libs-snapshot-local"
                )

                rtMavenResolver (
                    id: "MAVEN_RESOLVER",
                    serverId: "ARTIFACTORY_SERVER",
                    releaseRepo: "libs-release",
                    snapshotRepo: "libs-snapshot"
                )
            
        }

        stage ('Exec Maven') {
            
                rtMavenRun (
                    tool: 'maven-3.5.4', // Tool name from Jenkins configuration
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: "MAVEN_DEPLOYER",
                    resolverId: "MAVEN_RESOLVER"
                )
            
        }

        stage ('Publish build info') {
            
                rtPublishBuildInfo (
                    serverId: "Jfrog_artifactory"
                )
            }
        
}
