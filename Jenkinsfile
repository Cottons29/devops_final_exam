pipeline {
    agent {
        label 'lypheng-agent'
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        DEPLOY_DIR = "${WORKSPACE}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build -x test'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'ansible-playbook ansible/playbook.yml'
            }
        }
    }

    post {
        failure {
            emailext(
                subject: "BUILD FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: '''<p><strong>Build Failed</strong></p>
                    <p>Job: ${JOB_NAME}<br/>
                    Build Number: ${BUILD_NUMBER}<br/>
                    Build URL: <a href="${BUILD_URL}">${BUILD_URL}</a></p>
                    <p>Check the <a href="${BUILD_URL}console">console output</a> for details.</p>''',
                to: 'lycuttons@gmail.com',
                recipientProviders: [culprits()],
                mimeType: 'text/html'
            )
        }
        success {
            echo 'Build & Deploy successful!'
        }
    }
}
