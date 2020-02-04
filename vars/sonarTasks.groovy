import org.gs.stpl.java.SonarQube
import org.gs.stpl.util.Sonar
def call(String sonarTool, boolean qualityGate) {
 println (qualityGate)
 println (sonarTool)
 println (qualityGate.getClass())
 /*Sonar sonar = new Sonar (map.get(sonarTool), map.get(qualityGate))
 SonarQube sonarqube
  sonarqube.sonarAnalysis(sonar)*/  
}
