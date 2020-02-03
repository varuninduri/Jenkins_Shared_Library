def call(Map config, boolean check = false) {
    println "printing sonarserver"+config.sonarserver
    withSonarQubeEnv('sonar') {
        sh 'mvn sonar:sonar'
    } // SonarQube taskId is automatically attached to the pipeline context
  if (config.qualitycheck!=check){
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
