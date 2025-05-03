pipeline {
  agent any
    tools {
    // Install Maven and Jdk version configured as “M3” & “JDK”, add it to the path.
      maven ‘M3’
      jdk ‘JDK’
    }

  stages {
    stage(‘Checkout Code’) {
      steps {
        // Get code from a GitHub repository
        git branch: 'main',
            url: 'https://github.com/TQ-auto/test-ninja-site.git'
      }
    }

    stage(‘Start Docker and Pull Images’) {
      steps {
          script {
            bat ‘docker-compose -f docker-compose.yml up -d’
          }
        }
      }
    
    stage(‘Test code’){
      steps{
            bat “mvn compile”
            bat “mvn clean test ”
          }
        }
      }

  post{
    always{
      script {
          bat ‘docker stop {images name}’
        }
      }
    }
}
