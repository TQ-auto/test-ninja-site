pipeline {

  agent any 

    stages {
      stage('build') {
        steps {
          echo 'building the application...'
          echo 'Apply changes...'
        }
      }
      stage('test'){
        steps{
          echo 'testing the application...'
          sh './mvn test'
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
