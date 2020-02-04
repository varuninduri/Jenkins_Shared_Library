package org.gs.stpl.java

import org.gs.stpl.util.Sonar

/**
 * Class for SonarQube actions for the JavaPipeline.
 */
class SonarQube implements Serializable {

    

    /**
     * Execute a Sonar Analysis for this application. <br>
     * It will generate a sonar.properties file before running the analysis.
     * @param pom the maven pom, retrieved from readMavenPom()
     * @param sonarSkipModules a semi-colon separated list of maven modules that should not be analysed
     * @param util Utilities class
     */
    /*void sonarAnalysis(Sonar sonar) {
        assert sonar.sonarTool: 'Tool is not present!'
        assert sonar.qualityGate: 'Quality gate is not present!'
        
        def sonarInstallation = sonar.sonarTool
	boolean qualityCheck = sonar.qualityGate    
      
	println "running sonar"
        withSonarQubeEnv(sonarInstallation) {
        sh 'mvn sonar:sonar'
    } // SonarQube taskId is automatically attached to the pipeline context
  if (qualityCheck==true){
stage("Quality Gate"){
    timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
    def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
    if (qg.status != 'OK') {
        error "Pipeline aborted due to quality gate failure: ${qg.status}"
    }
  }
}
  }
                
    }*/
	def sonarAnalysis() {
		println "running sonar"
        withSonarQubeEnv('sonar') {
        sh 'mvn sonar:sonar'
    } // SonarQube taskId is automatically attached to the pipeline context
	}
}
