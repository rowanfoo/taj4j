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
                git (branch: 'master',url: 'https://github.com/rowanfoo/basej.git')

sh 'pwd'
sh 'ls'
              sh 'mvn -version'
              sh 'mvn compile'
              sh 'mvn install -DskipTests'

             }



            }
        }

        stage('Compile BASE') {
            steps {
                 sh 'mkdir -p project3'
             dir('project3'){
                git (branch: 'master',url: 'https://github.com/rowanfoo/base.git')


              sh 'mvn -version'
              sh 'mvn compile'
              sh 'mvn install -DskipTests'

             }



            }
        }


        stage('Compile BASE-Manager') {
            steps {
                 sh 'mkdir -p project3'
             dir('project3'){
                git (branch: 'master',url: 'https://github.com/rowanfoo/basemag.git')


              sh 'mvn -version'
              sh 'mvn compile'
              sh 'mvn install -DskipTests'

             }



            }
        }


    }


}
