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

             }



            }
        }


    }


}
