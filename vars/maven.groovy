def call(Map config, boolean check = false) {
switch(config.step){
case "clean":
sh 'mvn clean';
break;
case "compile":
sh 'mvn compile';
break;
case "sonar":
sh 'mvn sonar:sonar';
break;
case "test":
sh 'mvn test';
break;
case "coverage":
sh 'mvn cobertura:cobertura';
break;
case "package":
sh 'mvn package';
break;
}
}
