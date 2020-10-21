pipeline {

 environment {
     dockerImage = ""
     image_name=""
     name="ta4j"
     portno="8080"
     targetport="10100"
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
                git (branch: 'history',url: 'https://github.com/rowanfoo/basej.git')

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
             dir('project3'){
                git (branch: 'history',url: 'https://github.com/rowanfoo/base.git')
              sh 'mvn -version'
              sh 'mvn compile'
              sh 'mvn install -DskipTests'

             }



            }
        }


        stage('Compile BASE-Manager') {
            steps {
             dir('project4'){
                git (branch: 'master',url: 'https://github.com/rowanfoo/basemag.git')
              sh 'mvn -version'
              sh 'mvn compile'
              sh 'mvn install -DskipTests'

             }



            }
        }



        stage('BUILD  TAG APP') {
            steps {
              sh 'mvn -version'
              sh 'mvn compile'
              sh 'mvn package -DskipTests'
            }
        }

        stage('DOCKER TIME'){
            steps{
                script {
                    image_name = "rowanf/taj:$BUILD_NUMBER"
                   dockerImage =  docker.build image_name
                    sh 'pwd'
                }
            }
         }

        stage('DEPLOY '){
            steps{
                script {
                    docker.withRegistry( '', "mydocker-cred" ) {
                        dockerImage.push()
                    }
                }
            }
        }


       stage('Build') {
            steps {
                sh 'ssh -p 1600 root@192.168.0.10 date'
                 sh "ssh -p 1600 root@192.168.0.10 ansible-playbook -vvv /home/rowan/myplaybook.yaml -e \"name=${name}\"  -e \"image_name=${image_name}\" -e \"portno=${portno}\" -e \"targetport=${targetport}\"  "
            }
       }


    }


}
