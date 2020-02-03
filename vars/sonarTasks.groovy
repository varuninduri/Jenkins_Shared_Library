import org.gs.stpl.java.SonarQube
import org.gs.stpl.util.Sonar
def call(String sonarTool, boolean qualityGate) {
 Sonar sonar = new Sonar (sonarTool, qualityGate)
 SonarQube sonarqube
 sonarTool = sonar.sonarTool
 qualityGate = sonar.qualityGate
  sonarqube.sonarAnalysis(Sonar sonar)  
}
