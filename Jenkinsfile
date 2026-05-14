pipeline {

    agent {
        label 'Windows-Agent'
    }

    tools {
        maven 'Maven'
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
                bat 'docker build -t shopping-cart-app .'
            }
        }

        stage('Run Docker Container') {
            steps {
                bat 'docker rm -f shopping-cart-container || exit 0'
                bat 'docker run -d -p 8085:8080 --name shopping-cart-container shopping-cart-app'
            }
        }

        stage('Verify Container') {
            steps {
                bat 'docker ps'
            }
        }
    }
}