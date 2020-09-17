pipeline {

 environment {
     dockerImage = ""
  }
    agent any

    tools {
        maven 'maven'
    }


    stages {
        stage('Compile Stage') {
            steps {
                sh 'echo hello'
                sh 'pwd'
                 sh 'mkdir -p project2'
             dir('project2'){
//                git branch: 'master',url: 'https://github.com/rowanfoo/basemag.git'
 checkout scm: ([
                     $class: 'GitSCM',
                     userRemoteConfigs: [[url: https://github.com/rowanfoo/basemag.git]],
                     branches: [[name: 'master']]
             ])
sh 'pwd'
sh 'ls'
             }
              sh 'mvn -version'
              sh 'pwd'
              sh 'mvn compile'



            }
        }


    }


}
