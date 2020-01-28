def call(Map config) {
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
	}
else if(config.step == "coverage"){
	sh 'mvn cobertura:cobertura'
	}
else if(config.step == "package"){
	sh 'mvn package'
	}
else{
	echo "please configure maven steps"
	}	
}
