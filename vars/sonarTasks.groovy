import org.gs.stpl.java.SonarQube
import org.gs.stpl.util.Sonar
def call(LinkedHashMap<> map) {
 println (map.get(qualityGate))
 println (map.get(sonarTool))
 println (map.get(qualityGate).getClass())
 /*Sonar sonar = new Sonar (map.get(sonarTool), map.get(qualityGate))
 SonarQube sonarqube
  sonarqube.sonarAnalysis(sonar)*/  
}
