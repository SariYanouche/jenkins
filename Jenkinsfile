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
                // Run tests on Windows (use 'sh' if on Linux)
                bat 'gradlew test'
                // Archive results
                junit 'build/test-results/test/*.xml'
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
                    waitForQualityGate abortPipeline: true
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
             // Clean up workspace to save space
            cleanWs()
        }
        failure {
            mail to: 'ls_yanouche@esi.dz',
                 subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                 body: "Something went wrong. Check console output at ${env.BUILD_URL}"
        }
        success {
            echo 'Pipeline Succeeded!'
        }
    }
}