import org.gs.stpl.java.SonarQube
import org.gs.stpl.util.Sonar
/*def call(def nodeParams) {

	node(nodeParams){
 println (nodeParams.qualityGate)
 println (nodeParams.sonarTool)
 println ((nodeParams.araqualityGate).getClass())
 Sonar sonar = new Sonar (map.get(sonarTool), map.get(qualityGate))
 SonarQube sonarqube
  sonarqube.sonarAnalysis(sonar)  
}
}*/
def call(Map tool) {
	withSonarQubeEnv(tool.sonarTool){
        sh 'mvn sonar:sonar'
	}
	if (tool.qualityGate==true){
stage("Quality Gate"){
    timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
    def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
    if (qg.status != 'OK') {
        error "Pipeline aborted due to quality gate failure: ${qg.status}"
    }
  }
}
  }	

}
