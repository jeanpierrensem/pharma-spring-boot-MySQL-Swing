pipeline {
    agent {
        dockerContainer {
            image 'maven:3.9.16-eclipse-temurin-21-alpine'
        }
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    environment {
        // Les tests tournent sur H2 en mémoire (voir src/test/resources/application-test.properties),
        // aucune base MySQL n'est nécessaire pour ce pipeline.
        SPRING_PROFILES_ACTIVE = 'test'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn -B -ntp clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B -ntp test'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn -B -ntp package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        // Étapes à activer plus tard selon l'infra cible (non incluses faute de Dockerfile
        // et de destination de déploiement définis pour l'instant) :
        //
        // stage('Docker build') {
        //     steps {
        //         sh 'docker build -t officine-backend:${GIT_COMMIT} .'
        //     }
        // }
        //
        // stage('Deploy') {
        //     when { branch 'master' }
        //     steps {
        //         sh './scripts/deploy.sh'
        //     }
        // }
    }

    post {
        failure {
            echo 'Build échoué — voir les logs ci-dessus.'
        }
        always {
            cleanWs()
        }
    }
}
