def call(Map config, boolean check = false) {
if(config.step == "install"){
	sh 'sudo npm install -g @oracle/ojet-cli'
	}
  }
