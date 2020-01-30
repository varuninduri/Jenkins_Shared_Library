import hudson.plugins.git.GitTool
import org.gs.stpl.java.Git
import org.gs.stpl.util.Utilities
import org.gs.stpl.java.model.JavaPipelineInfo

Utilities utilities = new Utilities(this)
		String scmType = getScmType(scm)
		Git git = new Git(this)
		git.git = tool name: 'GIT', type: 'hudson.plugins.git.GitTool' //TODO: Tool name should be in properties file and match install tool name.
JavaPipelineInfo javaPipelineInfo = new JavaPipelineInfo(this, utilities, scmType)
		javaPipelineInfo.isMainlineBuild = utilities.isMainLineBranch(scmType, env.BRANCH_NAME)
	stage('Checkout') {
			checkout scm
		}
/*
def call(Map stageParams) {
checkout([
	$class: 'GitSCM',
	branches: [[name: stageParams.branch]],
	userRemoteConfigs: [[credentialsId: stageParams.credentials, url: stageParams.url]]
	])
}
*/
