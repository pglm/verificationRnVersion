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
    static final String EXTENSION_NAME = 'rnPackageJson'

    @Override
    void apply(Project project) {
        def ext = project.extensions.create(EXTENSION_NAME, PackageJson.class)



        project.task('verificationRnVersion') {
            println("------------0000")

            doFirst {
                println("------------${ext.localRnPath}")
                def packageJsonFile = new File(ext.localRnPath)
                def packageJson = new JsonSlurper().parseText(packageJsonFile.text)
                def packageVvRn = packageJson.dependencies['vv-rn']
                println packageVvRn

                def vvRnPackageJsonFile = new File(ext.remoteRnPath)
                def vvRnPackageJson = new JsonSlurper().parseText(vvRnPackageJsonFile.text)
                def vvRnPackageVvRn = vvRnPackageJson['version']
                println vvRnPackageVvRn

                if (packageVvRn != vvRnPackageVvRn) {
                    throw RuntimeException("please update rn node_modules")
                }
            }
        }


        project.tasks.whenTaskAdded { Task theTask ->
            if (theTask.name.contains('app:assembleProdHttps') || theTask.name.contains('app:bundleProdHttps')){

                theTask.dependsOn('verificationRnVersion')
                theTask.mustRunAfter('verificationRnVersion')
            }
        }
    }
}

