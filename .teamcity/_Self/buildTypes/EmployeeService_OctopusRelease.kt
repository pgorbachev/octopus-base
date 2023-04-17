package _Self.buildTypes

import jetbrains.buildServer.configs.kotlin.v2019_2.*

object EmployeeService_OctopusRelease : Template({
    name = "Octopus Release"

    buildNumberPattern = "%BUILD_NUMBER%"
    maxRunningBuilds = 1

    params {
        param("OCTOPUS_RELEASE_TIMEOUT", "10")
    }

    steps {
        step {
            name = "Call GitHub action"
            id = "RUNNER_4500"
            type = "OctopusCallGitHubAction"
        }
    }
})
