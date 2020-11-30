package com.mc_android.verificationRnVersion

import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class PackageJson {
    String localRnPath
    String remoteRnPath
}

class VerificationRnVersion implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("------------>>0000")
        project.extensions.create("rnPackageJson", PackageJson)
        println("------------>>"+project.rnPackageJson.localRnPath)
        project.task('verificationRnVersion') {
            def packageJsonFile = new File(project.rnPackageJson.localRnPath)
            def packageJson = new JsonSlurper().parseText(packageJsonFile.text)
            def packageVvRn = packageJson.dependencies['vv-rn']
            println packageVvRn

            def vvRnPackageJsonFile = new File(project.rnPackageJson.remoteRnPath)
            def vvRnPackageJson = new JsonSlurper().parseText(vvRnPackageJsonFile.text)
            def vvRnPackageVvRn = vvRnPackageJson['version']
            println vvRnPackageVvRn

            if (packageVvRn != vvRnPackageVvRn) {
                throw RuntimeException("请去更新rn的node_modules包,然后再打包")
            }
        }


        project.tasks.whenTaskAdded { Task theTask ->
            if (theTask.name.contains('assemble')) {
                theTask.dependsOn("verificationRnVersion")
                theTask.mustRunAfter("verificationRnVersion")
            }
        }
    }
}

