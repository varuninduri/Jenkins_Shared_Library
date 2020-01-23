import hudson.plugins.git.GitTool
import org.gs.stpl.java.Git
import org.gs.stpl.java.SonarQube
import org.gs.stpl.java.model.JavaPipelineInfo
import org.gs.stpl.java.MavenBuild
import org.gs.stpl.java.model.MavenBuildInfo
import org.gs.stpl.util.Maven
import org.gs.stpl.util.Utilities

def call(def nodeParams) {

	node(nodeParams){

		Utilities utilities = new Utilities(this)
		String scmType = getScmType(scm)
		Git git = new Git(this)
		git.git = tool name: 'GIT', type: 'hudson.plugins.git.GitTool' //TODO: Tool name should be in properties file and match install tool name.


		JavaPipelineInfo javaPipelineInfo = new JavaPipelineInfo(this, utilities, scmType)
		javaPipelineInfo.isMainlineBuild = utilities.isMainLineBranch(scmType, env.BRANCH_NAME)

		MavenBuild mavenBuild
		Maven maven
		MavenBuildInfo mavenBuildInfo
		String tagName

		// HOUSE KEEPING
		currentBuild.description = "Build @${env.NODE_NAME}[${env.EXECUTOR_NUMBER}]"
		deleteDir()

		// THE PIPELINE
		stage('Checkout') {
			checkout scm
		}


		try {

			stage('Prepare Build Configuration') {
				def propsFileName = 'jenkins.properties'
				def propertiesFileExists = fileExists propsFileName
				if (propertiesFileExists) {
					mavenBuildInfo = processJenkinsProperties(utilities, propsFileName)
					mavenBuildInfo.branchName = env.BRANCH_NAME
					mavenBuildInfo.pom = readMavenPom()
				} else {
					error 'No jenkins.properties file found.'
				}

				def jdkTool = utilities.getJDKTool(mavenBuildInfo.jdkVersion)
				def mavenTool = utilities.getMavenTool(mavenBuildInfo.mavenVersion)
				maven = new Maven(this, mavenTool, jdkTool)
				mavenBuild = new MavenBuild(this, maven, mavenBuildInfo)

				tagName = mavenBuild.createBuildTag(mavenBuildInfo.pom)
				if (scmType == 'git') {
					git.cleanAndResetToBranch(mavenBuildInfo.branchName)
					git.createTag(tagName)
				}

				//mavenBuild.prepareSettingsXml()
			}

			stage('Build') {
				if (javaPipelineInfo.isMainlineBuild) {

					String version = mavenBuild.mavenBuild(mavenBuildInfo.pom)
					//TODO: Add summary report

					if (mavenBuildInfo.builderCredentialsId && scmType == 'git') { // Check if other tag pushing supported
						//git.pushTagToRepo(tagName, mavenBuildInfo.builderCredentialsId) //currently failing
						//TODO: Add summary report
					} else {
						println "****WARNING*******: Did not find env.builderCredentialsId, cannot push tag!"
						//TODO: Add summary report
					}
				} else { // Non-Mainline
					mavenBuild.mavenBranchBuild()
					//TODO: Add summary report
				}

			}

			if (javaPipelineInfo.isMainlineBuild) {
				stage('Sonar') {
					
					println "running sonar"
					withSonarQubeEnv(mavenBuildInfo.sonarInstallation) {  //hard coded
						maven.mvn("sonar:sonar -Dsonar.links.ci=${env.BUILD_URL}")
					}
					//SonarQube sonarQube = new SonarQube(this)
					//sonarQube.sonarAnalysis(mavenBuildInfo, utilities, maven)
				}

				stage('Quality Gate') {
					timeout(time: 300, unit: 'SECONDS') {

						def qualitygate = waitForQualityGate()
						if (qualitygate.status != "OK") {
							error "Pipeline aborted due to quality gate coverage failure: ${qualitygate.status}"
						}
					}
				}
			}


		}finally {
			//TODO: Call Publish Summery Report
			println "finally"
			//stage('CleanUp') {
			//    step([$class: 'WsCleanup', notFailBuild: true])
			//}
		}

	}
}

MavenBuildInfo processJenkinsProperties(Utilities util, String propsFileName) {
	def jenkinsProperties
	timeout(time: 10, unit: 'SECONDS') {
		println "going to read file"
		jenkinsProperties = readProperties file: propsFileName //Need PipeLine Util plugin for readProperties
		println "read file"
	}

	println "properties are read file"
	MavenBuildInfo buildInfo = new MavenBuildInfo()
	buildInfo.deployable = util.getPropertyOrDefault(jenkinsProperties, 'deployable', 'false')
	buildInfo.jdkVersion = util.getPropertyOrDefault(jenkinsProperties, 'jdkVersion', '1.7')
	buildInfo.mavenVersion = util.getPropertyOrDefault(jenkinsProperties, 'mavenVersion', '3.3.3')
	buildInfo.buildTargets = util.getPropertyOrDefault(jenkinsProperties, 'buildTargets', '')
	if (util.getPropertyOrDefault(jenkinsProperties, 'publishJavaDoc', 'false') == 'true') {
		buildInfo.publishJavaDoc = true
	}
	buildInfo.javaDocBuildTargets = util.getPropertyOrDefault(jenkinsProperties, 'javaDocBuildTargets', '')
	buildInfo.javaDocFolder = util.getPropertyOrDefault(jenkinsProperties, 'javaDocFolder', 'target/site')
	if (util.getPropertyOrDefault(jenkinsProperties, 'publishJUnit', 'false') == 'true') {
		buildInfo.publishJUnit = true
	}
	buildInfo.snapshotDeployTarget = util.getPropertyOrDefault(jenkinsProperties, 'snapshotDeployTarget', '')
	if (util.getPropertyOrDefault(jenkinsProperties, 'archiveFiles', 'false') == 'true') {
		buildInfo.archiveFiles = true
	}
	buildInfo.archiveFilesSet = util.getPropertyOrDefault(jenkinsProperties, 'archiveFilesSet', '')
	buildInfo.sonarSkipModules = util.getPropertyOrDefault(jenkinsProperties, 'sonarSkipModules', ';')
	buildInfo.applicationCode = jenkinsProperties.applicationCode
	if (util.getPropertyOrDefault(jenkinsProperties, 'settingsXmlInWorkspace', 'false') == 'true') {
		buildInfo.settingsXmlInWorkspace = true
	}
	buildInfo.copySettingsXmlFromJob = util.getPropertyOrDefault(jenkinsProperties, 'copySettingsXmlFromJob', 'false')

	buildInfo.sonarInstallation = util.getPropertyOrDefault(jenkinsProperties, 'sonarInstallation', 'sonarqube')
	if (util.getPropertyOrDefault(jenkinsProperties, 'sonarQubeWithRunner', 'false') == 'true') {
		buildInfo.sonarQubeWithRunner = true
	}else {
		buildInfo.sonarQubeWithRunner = false
	}

	buildInfo.mavenMirrorUrl = util.getPropertyOrDefault(jenkinsProperties, 'mavenMirrorUrl', '')

	buildInfo.builderCredentialsId = util.getPropertyOrDefault(jenkinsProperties, 'builderCredentialsId', 'false')
	return buildInfo
}
