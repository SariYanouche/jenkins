pipeline {
    agent any

    environment {
        // This makes sure the variables we set in Jenkins are available here
        REPO_URL = "${REPO_URL}"
        REPO_USER = "${REPO_USER}"
        REPO_PASS = "${REPO_PASS}"
        scannerHome = tool 'SonarQubeScanner' // Ensure this name matches Manage Jenkins -> Tools
    }

    stages {
        stage('Test') {
                    steps {
                        bat 'gradlew test'

                        junit 'build/test-results/test/*.xml'

                        cucumber buildStatus: 'UNSTABLE',
                                 fileIncludePattern: '**/cucumber.json',
                                 jsonReportDirectory: 'build/reports'
                    }
                }

        stage('Code Analysis') {
            steps {
                withSonarQubeEnv('sonar') { // 'sonar' must match name in System Config
                    bat 'gradlew sonar'
                }
            }
        }

        stage('Code Quality') {
            steps {
                // Wait for SonarQube to tell us if it passed or failed
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false
                }
            }
        }

        stage('Build') {
            steps {
                bat 'gradlew build'
                // Archive the Jar file
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }

        stage('Deploy') {
            steps {
                bat 'gradlew publish'
            }
        }
    }

    post {
            always {
                cleanWs()
            }
            success {
                echo 'Pipeline Succeeded!'
                slackSend color: 'good',
                          message: "Build Success: ${currentBuild.fullDisplayName} (<${env.BUILD_URL}|Open>)"
            }
            failure {
                // Slack Notification
                slackSend color: 'danger',
                          message: "Build Failed: ${currentBuild.fullDisplayName} (<${env.BUILD_URL}|Open>)"
            }
        }
}