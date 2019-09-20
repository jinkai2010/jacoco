pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }
    tools {
        maven 'maven'
    }
    parameters {
        string(name: 'Git_Commit', defaultValue: 'master', description: 'branch/tag/commit')
        string(name: 'App_Ip', defaultValue: '127.0.0.1', description: '应用服务器ip')
        string(name: 'Agent_Port', defaultValue: '20000', description: '应用服务器agent端口')
        string(name: 'Project_ID', defaultValue: '200', description: '项目ID')
        string(name: 'Git_Url', defaultValue: 'http://eatools.bytedance.net/gitlab/purvar/wfc-agile.git', description: 'gitlab地址' )
        string(name:'sourcePath',defaultValue:'--sourcefiles /Users/jinkai/Documents/ByteDance_Git/wfc-agile/wfc-agile-biz/src/main/java --sourcefiles /Users/jinkai/Documents/ByteDance_Git/wfc-agile/wfc-agile-web/src/main/java --sourcefiles /Users/jinkai/Documents/ByteDance_Git/wfc-agile/wfc-agile-consumer/src/main/java --sourcefiles /Users/jinkai/Documents/ByteDance_Git/wfc-agile/wfc-agile-dal/src/main/java --sourcefiles /Users/jinkai/Documents/ByteDance_Git/wfc-agile/wfc-agile-common/src/main/java',description:'source目标，过个目录已空格隔开')
        string(name:'classPath',defaultValue:'--classfiles /Users/jinkai/Documents/ByteDance_Git/wfc-agile/wfc-agile-biz/target/classes --classfiles /Users/jinkai/Documents/ByteDance_Git/wfc-agile/wfc-agile-web/target/classes --classfiles /Users/jinkai/Documents/ByteDance_Git/wfc-agile/wfc-agile-consumer/target/classes --classfiles /Users/jinkai/Documents/ByteDance_Git/wfc-agile/wfc-agile-dal/target/classes --classfiles /Users/jinkai/Documents/ByteDance_Git/wfc-agile/wfc-agile-common/target/classes',description:'class目标，过个目录已空格隔开')

    }

    stages {
        stage('checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: "${params.Git_Commit}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'jinkai.2010_LDAP', url: '${Git_Url}']]])
            }
        }
        stage('compile') {
            steps {
                sh 'mvn -B -DskipTests clean compile'
            }
        }
        stage('jacoco dump2') {
            steps {
                echo "ip: ${params.App_Ip}"
                echo "${env.JOB_NAME},${env.WORKSPACE}"
                withAnt() {
                    sh "java -cp /Users/jinkai/Desktop/git-jacoco/org.jacoco.cli/target/org.jacoco.cli-0.8.5-SNAPSHOT-jar-with-dependencies.jar org.jacoco.cli.internal.Main dump --address ${params.App_Ip} --port ${params.Agent_Port} --destfile ./jacoco/${params.Git_Commit}.exec"
                }
            }
        }
        stage('jacoco report') {
            steps {
                sh "java -cp /Users/jinkai/Desktop/git-jacoco/org.jacoco.cli/target/org.jacoco.cli-0.8.5-SNAPSHOT-jar-with-dependencies.jar org.jacoco.cli.internal.Main report ./jacoco/${params.Git_Commit}.exec  ${params.classPath} ${params.sourcePath} --html ./jacoco/${params.Git_Commit} --commitid ${params.Git_Commit} --projectid ${params.Project_ID}"
            }
        }
        stage('report2') {
            steps {
                jacoco()
            }
        }

    }
}