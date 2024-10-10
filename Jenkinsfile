pipeline {
    agent {
        node{
            label 'docker-alpine-jdk'
        }
    }

    triggers {
        pollSCM('*/2 * * * *')
    }

    stages {
        stage('Build') {
            agent {
                docker {
                    image 'gradle:jdk17'
                }
            }
            steps {
                echo 'Building..'
                sh './gradlew build -xtest --build-cache --info'
            }
        }
        stage('Test') {
            agent {
                docker {
                    image 'gradle:jdk17'
                }
            }
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