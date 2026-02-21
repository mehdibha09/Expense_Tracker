pipeline {
    agent any

    options {
        skipDefaultCheckout()
    }

    tools {
        maven 'mvn'
        nodejs 'node'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'Git tok en', url: 'https://github.com/mehdibha09/Expense_Tracker.git'
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
                dir('expense-tracker-service') {
                    sh 'mvn test'
                }
            }
        }

stage('StartSecurityVM') {
    steps {
        sshagent(credentials: ['host-ssh-key']) {
            sh '''
            ssh -o StrictHostKeyChecking=no mehdi@192.168.1.15 << 'EOF'
STATE=$(VBoxManage showvminfo securite --machinereadable | grep VMState=)
if echo $STATE | grep -q poweroff; then
    echo "Starting Security VM"
    VBoxManage startvm securite --type headless
    sleep 15  # attendre que la VM démarre
else
    echo "Security VM already running"
fi
EOF
            '''
        }
    }
}

        stage('Wait for VM') {
            steps {
                echo 'Waiting 60 seconds for Security VM to boot...'
                sleep(time: 60, unit: 'SECONDS')
            }
        }

        stage('Sonar Analysis') {
            steps {
                dir('expense-tracker-service') {
                    withSonarQubeEnv('sonarqube-25.4.0.105899') {
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    script {
                        def qualityGate = waitForQualityGate()
                        if (qualityGate.status != 'OK') {
                            error "SonarQube Quality Gate failed: ${qualityGate.status}"
                        }
                        echo 'SonarQube analysis passed.'
                    }
                }
            }
        }

        stage('Stop Security VM') {
            steps {
                sshagent(credentials: ['host-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no mehdi@192.168.1.15 "
                        STATE=$(VBoxManage showvminfo securite --machinereadable | grep VMState=)
                        if echo $STATE | grep -q running; then
                            echo 'Stopping Security VM'
                            VBoxManage controlvm securite acpipowerbutton
                        else
                            echo 'Security VM already stopped'
                        fi
                    "
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Build was successful!'
        }
        failure {
            echo 'Build failed. Check logs.'
        }
    }
}