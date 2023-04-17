package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.freeDiskSpace
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.swabra
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.VcsTrigger
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

object OctopusGradleCompileUt : Template({
    name = "Octopus Gradle Compile & UT [W/O VCS Root]"

    allowExternalStatus = true

    params {
        text("GRADLE_STANDARD_PARAMETERS", "-Pversion=%build.number%", allowEmpty = true)
        param("WORK_DIR", "")
        param("JAVA_OPTS", "")
        param("CURRENT_COMMIT", "")
        param("PROJECT_VERSION", "")
    }

    steps {
        step {
            name = "Calculate build parameters"
            id = "RUNNER_4495"
            type = "OctopusCalculateBuildParameters"
        }
        gradle {
            name = "Build"
            id = "RUNNER_4463"
            tasks = "%GRADLE_TASK%"
            workingDir = "%WORK_DIR%"
            gradleParams = """
                %GRADLE_STANDARD_PARAMETERS%
                %GRADLE_EXTRA_PARAMETERS%
            """.trimIndent()
            enableStacktrace = true
            jdkHome = "%env.JAVA_HOME%"
            jvmArgs = """
                -Duser.name=%teamcity.agent.jvm.user.name% -Duser.home=%teamcity.agent.jvm.user.home%
                %JAVA_OPTS%
            """.trimIndent()
            param("org.jfrog.artifactory.selectedDeployableServer.defaultModuleVersionConfiguration", "GLOBAL")
        }
    }

    triggers {
        vcs {
            id = "TRIGGER_1763"
            quietPeriodMode = VcsTrigger.QuietPeriodMode.USE_DEFAULT
            branchFilter = ""
        }
        schedule {
            id = "TRIGGER_1764"
            schedulingPolicy = weekly {
                hour = 10
            }
            branchFilter = ""
            triggerBuild = always()
            withPendingChangesOnly = false
        }
    }

    features {
        swabra {
            id = "swabra"
        }
        freeDiskSpace {
            id = "jetbrains.agent.free.space"
            requiredSpace = "5gb"
            failBuild = false
        }
        dockerSupport {
            id = "DockerSupport"
            loginToRegistry = on {
                dockerRegistryId = "PROJECT_EXT_177"
            }
        }
    }

    requirements {
        exists("env.DOCKER", "RQ_2007")
        contains("env.OS_TYPE", "CENTOS7x64", "RQ_2008")
    }
})
