//pipeline {
//    agent any
//    tools {
//        maven 'Maven3'
//    }
//
//    environment {
//        DOCKERHUB_REPO = 'loooonnngg/sep2-shopping-cart'
//        DOCKER_IMAGE_TAG = 'latest'
//        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
//    }
//
//    stages {
//        stage('Build and Test') {
//            steps {
//                sh 'mvn -B clean verify'
//            }
//        }
//
//        stage('Build Docker Image') {
//            steps {
//                script {
//                    docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
//                }
//            }
//        }
//
//        stage('Push Docker Image to Docker Hub') {
//            steps {
//                script {
//                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
//                        docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
//                    }
//                }
//            }
//        }
//    }
//}
//

pipeline {
    agent any
    tools {
        maven 'Maven3'
    }
    environment {
        PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
        JAVA_HOME = 'C:\\Users\\minhl\\.jdks\\ms-21.0.10\\bin' // Adjust to your actual JDK pat
        SONARQUBE_SERVER = 'SonarQubeServer' // The name of the SonarQube server configured in Jenkins
        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
        DOCKERHUB_REPO = 'loooonnngg/sep2-shopping-cart'
        DOCKER_IMAGE_TAG = 'latest'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/long3011/SEP2-assignments.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    bat """
                    ${tool 'SonarScanner'}\\bin\\sonar-scanner ^
                    -Dsonar.projectKey=SEP2-assignments ^
                    -Dsonar.sources=src ^
                    -Dsonar.projectName=SEP2-assignments ^
                    -Dsonar.host.url=http://localhost:9000 ^
                    -Dsonar.login=${env.SONAR_TOKEN} ^
                    -Dsonar.java.binaries=target/classes
                    """
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                }
            }
        }
        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                        docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }
    }
}
