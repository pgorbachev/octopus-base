package _Self

import _Self.buildTypes.*
import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.Project

object Project : Project({
    description = "Octopus OSS components"

    template(OctopusGradleCompileUt)
    template(EmployeeService_OctopusRelease)

    params {
        param("zenit", "champion")
        param("spartak", "otstoi")
    }
})
