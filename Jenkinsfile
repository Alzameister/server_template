pipeline {
    agent {
        docker {
            image 'gradle:jdk21'
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