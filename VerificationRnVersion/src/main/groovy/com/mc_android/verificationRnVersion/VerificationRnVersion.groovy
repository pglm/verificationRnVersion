package com.mc_android.verificationRnVersion

import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class VerificationRnVersion implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.create("rnPackageJson", PackageJson)
        project.task('verificationRnVersion'){
            println "开始验证rn版本和module里面的版本是否一致"
            def packageJsonFile = new File(project.rnPackageJson.localRnPath)
            def packageJson = new JsonSlurper().parseText(packageJsonFile.text)
            def packageVvRn=packageJson.dependencies['vv-rn']
            println packageVvRn

            def vvRnPackageJsonFile = new File(project.rnPackageJson.remoteRnPath)
            def vvRnPackageJson  = new JsonSlurper().parseText(vvRnPackageJsonFile.text)
            def vvRnPackageVvRn=vvRnPackageJson['version']
            println vvRnPackageVvRn

            if(packageVvRn == vvRnPackageVvRn){
                println "rn版本和module里面的版本是一致的："+packageVvRn
            }else{
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


class PackageJson {
    String localRnPath = './module/rn/package.json'
    String remoteRnPath = './module/rn/node_modules/vv-rn/package.json'
}