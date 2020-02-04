import org.gs.stpl.java.SonarQube
import org.gs.stpl.util.Sonar
def call(String sonarTool, boolean qualityGate) {
 Sonar sonar = new Sonar (sonarTool, qualityGate)
 SonarQube sonarqube
  sonarqube.sonarAnalysis(sonar)  
}
