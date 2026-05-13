pipeline {

    agent {
        label 'Windows-Agent'
    }

    tools {
        maven 'Maven'
    }

    stages {

        stage('Workspace Cleanup') {
            steps {
                cleanWs()
            }
        }

        stage('Checkout Code') {
            steps {
                git branch: 'release/develop_release',
                url: 'https://github.com/Shubh-prajapati/shopping-cart-springboot.git'
            }
        }

        stage('Verify Java & Maven') {
            steps {
                bat 'java -version'
                bat 'mvn -version'
            }
        }

        stage('Build Application') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Verify Artifact') {
            steps {
                bat 'dir target'
            }
        }
    }

    post {

        success {
            echo 'Build Successful'
        }

        failure {
            echo 'Build Failed'
        }
    }
}
