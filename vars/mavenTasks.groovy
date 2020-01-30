def call(Map config, boolean check = false) {
if(config.step == "clean"){
	sh 'cd SchedulerDemo'
	sh 'mvn clean'
	}
else if(config.step == "compile"){
	if (config.containsKey("pomlocation")){
		def pomxmllocation = config.get("pomlocation")
		sh 'mvn -f pomxmllocation compile'
	}
	sh 'mvn -f "pomxmllocation" compile'
	}
else if(config.step == "sonar"){
	sh 'mvn sonar:sonar'
	}
else if(config.step == "test"){
	sh 'mvn test'
	
	if(config.testreport!=check){
	junit 'target/surefire-reports/*.xml'	
	}
	}
else if(config.step == "coverage"){
	sh 'mvn cobertura:cobertura'
	if(config.coveragereport!=check){
	cobertura autoUpdateHealth: false, autoUpdateStability: false, coberturaReportFile: 'target/site/cobertura/coverage.xml', conditionalCoverageTargets: '70, 0, 0', failUnhealthy: false, failUnstable: false, lineCoverageTargets: '80, 0, 0', maxNumberOfBuilds: 0, methodCoverageTargets: '80, 0, 0', onlyStable: false, sourceEncoding: 'ASCII', zoomCoverageChart: false
	}
	}
else if(config.step == "package"){
	sh 'mvn package'
	}
else if(config.step == "artifactory"){
	sh 'mvn deploy'
	}	
else{
	echo "please configure maven steps"
	}	
}
