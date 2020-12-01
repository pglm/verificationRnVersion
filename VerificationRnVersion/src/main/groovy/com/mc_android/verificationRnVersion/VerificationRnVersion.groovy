package com.mc_android.verificationRnVersion

import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class PackageJson {
    String localRnPath
    String remoteRnPath

    String getLocalRnPath() {
        return localRnPath
    }

    void setLocalRnPath(String localRnPath) {
        this.localRnPath = localRnPath
    }

    String getRemoteRnPath() {
        return remoteRnPath
    }

    void setRemoteRnPath(String remoteRnPath) {
        this.remoteRnPath = remoteRnPath
    }
}

class VerificationRnVersion implements Plugin<Project> {
    static final String EXTENSION_NAME = 'rnPackageJson'
    @Override
    void apply(Project project) {
        println("------------>>0000")
        def ext = project.extensions.create(EXTENSION_NAME, PackageJson.class)
        println("------------>>" + ext)
        println("------------>>" + ext.localRnPath)
        project.task('verificationRnVersion') {
            println("------------>>222222" + ext.localRnPath)
            doLast {
                println("------------>>3333" + ext.localRnPath)
            }
            def packageJsonFile = new File(ext.localRnPath)
            def packageJson = new JsonSlurper().parseText(packageJsonFile.text)
            def packageVvRn = packageJson.dependencies['vv-rn']
            println packageVvRn

            def vvRnPackageJsonFile = new File(ext.remoteRnPath)
            def vvRnPackageJson = new JsonSlurper().parseText(vvRnPackageJsonFile.text)
            def vvRnPackageVvRn = vvRnPackageJson['version']
            println vvRnPackageVvRn

            if (packageVvRn != vvRnPackageVvRn) {
                throw RuntimeException("请去更新rn的node_modules包,然后再打包")
            }
        }


//        project.tasks.whenTaskAdded { Task theTask ->
//            if (theTask.name.contains('assemble')) {
//                theTask.dependsOn('verificationRnVersion')
//                theTask.mustRunAfter('verificationRnVersion')
//            }
//        }
    }
}

