pipeline {
    agent any
    stages {
        stage("Build") {
            steps {
                sh "docker-compose down"
                tool 'Maven 3.6.3'
                sh "mvn clean install -DskipTests"
            }
        }
        stage("Test") {
            steps {
                // Ignorer les tests dans cette étape
                echo 'Tests ignorés'
            }
        }
        stage("Run") {
            steps {
                script {
                    sh "docker-compose up --build -d"
                }
            }
        }
    }
}
