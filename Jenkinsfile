pipeline {
    agent {
        docker {
            image 'gradle:jdk-21-and-22'
            label 'docker'
        }

        node {
            label 'jenkins-node-goes-here'
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