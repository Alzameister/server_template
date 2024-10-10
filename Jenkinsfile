pipeline {
    agent {
        node{
            label 'docker-alpine-jdk'
        }
        docker {
            image 'gradle:jdk17'
        }
    }

    triggers {
        pollSCM('*/2 * * * *')
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh './gradlew build -xtest --build-cache --info'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh './gradlew test'
            }
        }
    }

    post {
        always {
            echo 'Cleaning up..'
            cleanWs()
        }
    }
}