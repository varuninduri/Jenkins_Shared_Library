def call(Map config, boolean check = false) {
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
	if(config.typeBuild=="jar"){
	sh 'mvn package'
	}
	else if(config.typeBuild=="war"){
	sh 'mvn war:war'
	}
	else{
	echo "please define type of Build with param typeBuild"
	}
	}
else{
	echo "please configure maven steps"
	}	
}
