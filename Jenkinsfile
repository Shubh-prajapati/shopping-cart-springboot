pipeline {

    agent {
        label 'Windows-Agent'
    }

    tools {
        maven 'Maven'
    }

    environment {
        IMAGE_NAME = "shubh7707/shopping-cart-app:v1"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'release/develop_release',
                url: 'https://github.com/Shubh-prajapati/shopping-cart-springboot.git'
            }
        }

        stage('Build Application') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %IMAGE_NAME% .'
            }
        }

        stage('DockerHub Login') {
            steps {
                withCredentials([usernamePassword(
                   credentialsId: 'dockerhub-login'
                    usernameVariable: 'DOCKER_USER',
                     passwordVariable: 'DOCKER_PASS'
                )]) {

                    bat 'echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                bat 'docker push %IMAGE_NAME%'
            }
        }
    }
}