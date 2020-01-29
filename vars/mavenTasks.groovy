def call(Map config, boolean abc = false) {
if(config.step == "clean"){
	sh 'mvn clean'
	}
else if(config.step == "compile"){
	sh 'mvn compile'
	}
else if(config.step == "sonar"){
	sh 'mvn sonar:sonar'
	}
else if(config.step == "test"){
	sh 'mvn test'
	
	if(config.testreport!=abc){
	junit 'target/surefire-reports/*.xml'	
	}
	}
else if(config.step == "coverage"){
	sh 'mvn cobertura:cobertura'
	if(config.coveragereport!=abc){
	cobertura autoUpdateHealth: false, autoUpdateStability: false, coberturaReportFile: 'target/site/cobertura/coverage.xml', conditionalCoverageTargets: '70, 0, 0', failUnhealthy: false, failUnstable: false, lineCoverageTargets: '80, 0, 0', maxNumberOfBuilds: 0, methodCoverageTargets: '80, 0, 0', onlyStable: false, sourceEncoding: 'ASCII', zoomCoverageChart: false
	}
	}
else if(config.step == "package"){
	sh 'mvn package'
	}
else{
	echo "please configure maven steps"
	}	
}
