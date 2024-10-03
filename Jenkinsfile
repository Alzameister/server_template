pipeline {
    agent {
        docker {
            image 'gradle:8.2.0-jdk17-alpine'
            label 'docker-agent-jdk'
            reuseNode true
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