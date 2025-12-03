// This is the Jenkinsfile that will be used to build & test the project.
pipeline {
    environment {
        RENDER_API_KEY = credentials('render-api-key')
        // Replace with the backend deploy hook you copied
        RENDER_BACKEND_DEPLOY_HOOK = "https://api.render.com/deploy/srv-d4g7rih5pdvs73a0p03g?key=Fb5-LPdrHNA"
        // Replace with the frontend deploy hook you copied
        RENDER_FRONTEND_DEPLOY_HOOK = "https://api.render.com/deploy/srv-d4g8m4vgi27c73bbdrlg?key=B2ksgpaa-vE"
    }
    agent any
    options {
        skipDefaultCheckout()
    }
    tools {
        maven "mvn"
        nodejs "node"
    }


    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'Git token', url: 'https://github.com/mehdibha09/Expense_Tracker.git'
            }
        }
        stage('Build') {
            parallel {
                stage('Java') {
                    steps {
                        dir('expense-tracker-service') {
                            sh 'mvn clean install'
                        }
                    }
                }

                stage('Angular') {
                    steps {
                        dir('expense-tracker-ui') {
                            sh 'npm install'
                            sh './node_modules/.bin/ng build --configuration production'
                        }
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    sh 'cd expense-tracker-service && mvn test'
                }
            }
        }
    }
    post {
        success {
            // Actions after the build succeeds
            echo 'Build was successful!'
        }
        failure {
            // Actions after the build fails
            echo 'Build failed. Check logs.'
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
