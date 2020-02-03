package org.gs.stpl.util

/**
 * Class for Maven specific utilities that can be used by all pipelines.
 */
class Sonar implements Serializable {

    String sonarTool
    boolean qualityGate

    /**
     * Creates a new Maven utility class.
     *
     * @param steps the pipeline dsl context
     * @param mavenTool string of the mavenTool (retrieved via the 'tool' dsl command)
     * @param jdkTool string of the jdkTool (retrieved via the 'tool' dsl command)
     */
    Sonar(String sonarTool, String qualityGate) {
        assert SonarTool: 'I need sonarTool to be valid'
        assert qualityGate: 'I need qualityGate to be valid'

        this.sonarTool = mavenTool
        this.qualityGate = qualityGate
    }
    }
