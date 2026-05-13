pipeline {

    agent {
        label 'Windows-Agent'
    }

    stages {

        stage('Verify Tools') {
            steps {
                bat 'git --version'
                bat 'java -version'
                bat 'mvn -version'
            }
        }

        stage('Checkout Code') {
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

        stage('Verify Artifact') {
            steps {
                bat 'dir target'
            }
        }
    }
}
