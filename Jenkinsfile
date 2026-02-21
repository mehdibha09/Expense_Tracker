pipeline {
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
                script {
                    sh 'cd expense-tracker-service && mvn test'
                }
            }
        }

    //Add this code for sonar waitForQualityGate.
      post {
          success {
              script {
                  timeout(time: 2, unit: 'MINUTES') {
                      def qualityGate = waitForQualityGate()
                      if (qualityGate.status != 'OK') {
                          error "SonarQube Quality Gate failed: ${qualityGate.status}"
                      } else {
                          echo "SonarQube analysis passed."
                      }
                  }
              }
          }
          failure {
              echo "SonarQube analysis failed during execution."
          }
      }
}
   stage('StartSecurityVM') {
    steps {
        sshagent(credentials: ['host-ssh-key']) {
            sh '''
            ssh -o StrictHostKeyChecking=no mehdi@192.168.1.15 "
                STATE=$(VBoxManage showvminfo securite --machinereadable | grep VMState=)
                if echo $STATE | grep -q poweroff; then
                    echo 'Starting Security VM'
                    VBoxManage startvm securite --type headless
                else
                    echo 'Security VM already running'
                fi
            "
            '''
        }
    }
}
        // attendre que la VM soit complètement opérationnelle
stage('WaitForVM') {
    steps {
        echo 'Waiting 60 seconds for Security VM to boot...'
        sleep(time: 60, unit: 'SECONDS')
    }
}

stage('Sonar') {
      steps {
          dir('expense-tracker-service') {
              withSonarQubeEnv('sonarqube-25.4.0.105899') {
                  sh 'mvn sonar:sonar'
              }
          }
      }
stage('StopSecurityVM') {
    steps {
        sshagent(credentials: ['host-ssh-key']) {
            sh '''
            ssh -o StrictHostKeyChecking=no user@IP_MACHINE_HOTE "
                STATE=$(VBoxManage showvminfo SecurityVM --machinereadable | grep VMState=)
                if echo $STATE | grep -q running; then
                    echo 'Stopping Security VM'
                    VBoxManage controlvm SecurityVM acpipowerbutton
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
            // Actions after the build succeeds
            echo 'Build was successful!'
        }
        failure {
            // Actions after the build fails
            echo 'Build failed. Check logs.'
        }
    }
}