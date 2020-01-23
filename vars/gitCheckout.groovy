
def call(Map stageParams) {
checkout([
	$class: 'GitSCM',
	branches: [[name: stageParams.branch]],
	userRemoteConfigs: [[credentialsId: 'bitbucket_gaurav', url: stageParams.url]]
	])
}