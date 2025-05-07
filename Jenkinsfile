pipeline {
  agent any
    tools {
    // Install Maven and Jdk version configured as “M3” & “JDK”, add it to the path.
      maven 'Maven-installation'
      jdk 'Java-JDK'
      dockerTool 'myDocker'
    }

  stages {
    stage('Checkout Code') {
      steps {
        // Get code from a GitHub repository
        git branch: 'main',
            credentialsId: '8efcb49c-3964-4e78-8392-e59cdcd00457',
            url: 'https://github.com/TQ-auto/test-ninja-site.git'
      }
    }

    stage('Start Docker and Pull Images') {
      steps {
          script {
            sh 'docker-compose up'
          }
        }
      }
    
    stage('Test code'){
      steps{
            sh 'mvn compile'
            sh 'mvn clean test'
          }
        }
    }

  post{
    always{
      script {
          sh 'docker-compose down'
        }
      }
    }
}
