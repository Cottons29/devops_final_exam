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
                git url: 'https://github.com/Cottons29/devops_final_exam.git',
                    branch: 'main'
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
                subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: '''<h2 style="color:red;">Build Failed</h2>
                    <table>
                        <tr><td><b>Job:</b></td><td>${JOB_NAME}</td></tr>
                        <tr><td><b>Build #:</b></td><td>${BUILD_NUMBER}</td></tr>
                        <tr><td><b>Build URL:</b></td><td><a href="${BUILD_URL}">${BUILD_URL}</a></td></tr>
                        <tr><td><b>Console:</b></td><td><a href="${BUILD_URL}console">View Logs</a></td></tr>
                    </table>
                    <p>Check the console output for details.</p>''',
                to: 'srengty@gmail.com',
                recipientProviders: [culprits(), developers()],
                mimeType: 'text/html'
            )
        }
        success {
            emailext(
                subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: '''<h2 style="color:green;">Build Successful</h2>
                    <table>
                        <tr><td><b>Job:</b></td><td>${JOB_NAME}</td></tr>
                        <tr><td><b>Build #:</b></td><td>${BUILD_NUMBER}</td></tr>
                        <tr><td><b>Build URL:</b></td><td><a href="${BUILD_URL}">${BUILD_URL}</a></td></tr>
                        <tr><td><b>Console:</b></td><td><a href="${BUILD_URL}console">View Logs</a></td></tr>
                    </table>
                    <p>Deployment to web server completed successfully.</p>''',
                to: 'lycuttons@gmail.com',
                recipientProviders: [culprits(), developers()],
                mimeType: 'text/html'
            )
        }
    }
}
