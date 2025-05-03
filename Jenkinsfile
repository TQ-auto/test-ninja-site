pipeline {
  agent any 
    stages {
      stage('Checkout Code'){
        steps{
          echo 'Checkout Code'
          git branch: 'main',
            url: 'https://github.com/TQ-auto/test-ninja-site.git'
        }
      }
      stage('build') {
        steps {
          echo 'building the application...'
          echo 'mvn clean'
          sh 'mvn -B -DskipTests clean package'
        }
      }
      stage('test'){
        steps{
          echo 'testing the application...'
          sh 'mvn test'
        }
      }
      stage("deploy"){
          steps {
            echo 'deploying the application...'
          }
        }
      }
}

node {
  // groovy script
}
