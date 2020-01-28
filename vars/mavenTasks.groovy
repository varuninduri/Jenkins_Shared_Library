def call(Map config) {
if(config.step == "clean"){
	sh 'mvn clean'
	}
}
