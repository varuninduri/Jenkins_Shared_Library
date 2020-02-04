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
	println tool.sonarTool
	println tool.qualityGate instanceof boolean
	println tool.qualityGate.getClass()
	withSonarQubeEnv(tool.sonarTool){
        sh 'mvn sonar:sonar'
}
}
