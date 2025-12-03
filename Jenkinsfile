
pipeline {
    agent any

    environment {
        RENDER_API_KEY = credentials('render-api-key')
        RENDER_BACKEND_DEPLOY_HOOK = "https://api.render.com/deploy/srv-d4g7rih5pdvs73a0p03g?key=Fb5-LPdrHNA"
        RENDER_FRONTEND_DEPLOY_HOOK = "https://api.render.com/deploy/srv-d4g8m4vgi27c73bbdrlg?key=B2ksgpaa-vE"
    }

    tools {
        jdk "jdk17"
        maven "Maven"
        nodejs "node20"
    }

    options {
        skipDefaultCheckout()
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'Git token',
                    url: 'https://github.com/mehdibha09/Expense_Tracker.git'
            }
        }

        stage('Build') {
            parallel {

                stage('Java Backend') {
                    steps {
                        dir('expense-tracker-service') {
                            sh 'java -version'
                            sh 'mvn -version'
                            sh 'mvn clean install'
                        }
                    }
                }

                stage('Angular Frontend') {
                    steps {
                        dir('expense-tracker-ui') {
                            sh 'node -v'
                            sh 'npm -v'
                            sh 'npm install'
                            sh './node_modules/.bin/ng build --configuration production'
                        }
                    }
                }
            }
        }

        stage('Test Backend') {
            steps {
                sh 'cd expense-tracker-service && mvn test'
            }
        }

        stage('Deploy to Render') {
            steps {
                script {
                    echo "Deploying Backend..."
                    def backendResponse = httpRequest(
                        url: "${RENDER_BACKEND_DEPLOY_HOOK}",
                        httpMode: 'POST',
                        validResponseCodes: '200:299'
                    )
                    echo "Render Backend Deployment Response: ${backendResponse}"

                    echo "Deploying Frontend..."
                    def frontendResponse = httpRequest(
                        url: "${RENDER_FRONTEND_DEPLOY_HOOK}",
                        httpMode: 'POST',
                        validResponseCodes: '200:299'
                    )
                    echo "Render Frontend Deployment Response: ${frontendResponse}"
                }
            }
        }
    }

    post {
        success {
            echo '✔ Build + Deploy completed successfully!'
        }
        failure {
            echo '❌ Build failed — check logs.'
        }
    }
}
