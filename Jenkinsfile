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

                git branch: 'master',
                             url: 'https://github.com/rowanfoo/basemag.git'
              sh 'mvn -version'
                sh 'mvn compile'
            }
        }


    }


}
