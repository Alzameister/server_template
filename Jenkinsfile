pipeline {
    agent {
        docker {image 'gradle:jdk17'}
    }

    triggers {
        pollSCM('*/2 * * * *')
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh './gradlew'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                sh './gradlew test'
            }
        }
    }
}