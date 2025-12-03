pipeline {
    agent any

    environment {
        RENDER_API_KEY = credentials('render-api-key')
        RENDER_BACKEND_DEPLOY_HOOK = "https://api.render.com/deploy/srv-d4g7rih5pdvs73a0p03g?key=Fb5-LPdrHNA"
        RENDER_FRONTEND_DEPLOY_HOOK = "https://api.render.com/deploy/srv-d4g8m4vgi27c73bbdrlg?key=B2ksgpaa-vE"
        SONAR_TOKEN = credentials('sonoarToken')
    }

    tools {
        maven "mvn"
        nodejs "node"
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

        stage('Sonar') {
            steps {
                dir('expense-tracker-service') {
                    withSonarQubeEnv('sonoarQube') {
                        sh """
                        mvn clean compile sonar:sonar \
                            -Dsonar.projectKey=expenseTracker \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.login=${SONAR_TOKEN} \
                            -Dsonar.java.binaries=target/classes
                        """
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    // Fonction retry si timeout dépassé
                    def retryForTimeoutExceeded = { count = 3, Closure closure ->
                        for (int i = 1; i <= count; i++) {
                            try {
                                closure()
                                break
                            } catch (Exception error) {
                                // On peut filtrer sur le message si on veut
                                def hasTimeoutExceeded = error.toString().contains("ExceededTimeout")
                                int retriesLeft = count - i
                                echo "Timeout exceeded for Quality Gate. Retries left: ${retriesLeft}"
                                if (retriesLeft == 0 || !hasTimeoutExceeded) {
                                    throw error
                                } else {
                                    sleep(time: 5, unit: 'SECONDS')
                                }
                            }
                        }
                    }


                    retryForTimeoutExceeded {
                        timeout(time: 10, unit: 'MINUTES') {
                            def qg = waitForQualityGate()
                            if (qg.status != 'OK') {
                                error "Pipeline aborted due to SonarQube Quality Gate failure: ${qg.status}"
                            } else {
                                echo "✔ SonarQube Quality Gate passed."
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy to Render') {
            steps {
                script {
                    echo "Deploying Backend..."
                    sh "curl -X POST ${RENDER_BACKEND_DEPLOY_HOOK}"

                    echo "Deploying Frontend..."
                    sh "curl -X POST ${RENDER_FRONTEND_DEPLOY_HOOK}"
                }
            }
        }
    }

    post {
        success {
            echo '✔ Build + Sonar + Deploy completed successfully!'
        }
        failure {
            echo '❌ Build failed — check logs.'
        }
    }
}
