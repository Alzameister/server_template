pipeline {
    agent {
        docker {
            image 'gradle:8.7-jdk-21-and-22-alpine'
            label 'docker-agent-jdk'
        }
    }

    triggers {
        pollSCM('*/2 * * * *')
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
    }
}