pipeline {
    agent {
        docker {'image: jenkins/agent:alpine-jdk21'}
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

    post {
        always {
            echo 'Cleaning up..'
            cleanWs()
        }
    }
}