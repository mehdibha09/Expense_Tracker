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

        // stage('Build') {
        //     parallel {
        //         stage('Java') {
        //             steps {
        //                 dir('expense-tracker-service') {
        //                     sh 'mvn clean install'
        //                 }
        //             }
        //         }

        //         stage('Angular') {
        //             steps {
        //                 dir('expense-tracker-ui') {
        //                     sh 'npm install'
        //                     sh './node_modules/.bin/ng build --configuration production'
        //                 }
        //             }
        //         }
        //     }
        // }

        // stage('Test') {
        //     steps {
        //         dir('expense-tracker-service') {
        //             sh 'mvn test'
        //         }
        //     }
        // }

//         stage('Start Security VM') {
//             steps {
//                 sh '''
//                 ssh -i /var/jenkins_home/.ssh/id_rsa_vmjenkins_nopass -o StrictHostKeyChecking=no mehdi@192.168.1.15 << 'EOF'
// STATE=$(VBoxManage showvminfo securite --machinereadable | grep VMState=)
// if echo $STATE | grep -q poweroff; then
//     echo "Starting Security VM"
//     VBoxManage startvm securite --type headless
//     sleep 15
// else
//     echo "Security VM already running"
// fi
// EOF
//                 '''
//             }
//         }

        // stage('Wait for VM') {
        //     steps {
        //         echo 'Waiting 60 seconds for Security VM to boot...'
        //         sleep(time: 60, unit: 'SECONDS')
        //     }
        // }

        // stage('Sonar Analysis') {
        //     steps {
        //         dir('expense-tracker-service') {
        //             withSonarQubeEnv('SonarQubeScanner') {
        //                 sh 'mvn sonar:sonar'
        //             }
        //         }
        //     }
        //     post {
        //         success {
        //             script {
        //                 timeout(time: 2, unit: 'MINUTES') {
        //                     def qualityGate = waitForQualityGate()
        //                     if (qualityGate.status != 'OK') {
        //                         error "SonarQube Quality Gate failed: ${qualityGate.status}"
        //                     } else {
        //                         echo "SonarQube analysis passed."
        //                     }
        //                 }
        //             }
        //         }
        //         failure {
        //             echo "SonarQube analysis failed during execution."
        //         }
        //     }
        // }

//    stage('Build Docker Images') {
//     steps {
//         // Backend
//         dir('expense-tracker-service') {
//             sh 'docker build -t my-nexus-repo/expense-backend:latest .'
//         }

//         // Frontend
//         dir('expense-tracker-ui') {
//             sh 'docker build -t my-nexus-repo/expense-frontend:latest .'
//         }
//     }
// }

            // stage('Push Docker Images to Nexus') {
            //     steps {
            //         withCredentials([usernamePassword(
            //             credentialsId: 'nexus-creds',
            //             usernameVariable: 'NEXUS_USER',
            //             passwordVariable: 'NEXUS_PASSWORD'
            //         )]) {
            //             sh '''
            //             echo $NEXUS_PASSWORD | docker login 192.168.56.30:8082 -u $NEXUS_USER --password-stdin
            //             docker tag my-nexus-repo/expense-backend:latest 192.168.56.30:8082/expense-backend:latest
            //             docker tag my-nexus-repo/expense-frontend:latest 192.168.56.30:8082/expense-frontend:latest
            //             docker push 192.168.56.30:8082/expense-backend:latest
            //             docker push 192.168.56.30:8082/expense-frontend:latest
            //             '''
            //         }
            //     }
            // }

            stage('Security Scan') {
                agent { label 'Security' }
                steps {
                    withCredentials([usernamePassword(
                        credentialsId: 'nexus-creds',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASSWORD'
                    )]) {
                        sh '''
                        echo $NEXUS_PASSWORD | docker login 192.168.56.30:8082 -u $NEXUS_USER --password-stdin
                        docker pull 192.168.56.30:8082/expense-backend:latest
                        docker pull 192.168.56.30:8082/expense-frontend:latest
                        docker run --rm \
                          -v /var/run/docker.sock:/var/run/docker.sock \
                          -v /mnt/nfs/trivy-results:/results \
                          aquasec/trivy image \
                          --exit-code 1 --severity HIGH,CRITICAL \
                          --format json \
                          --output /results/expense-backend.json \
                          192.168.56.30:8082/expense-backend:latest
                        docker run --rm \
                          -v /var/run/docker.sock:/var/run/docker.sock \
                          -v /mnt/nfs/trivy-results:/results \
                          aquasec/trivy image \
                          --exit-code 1 --severity HIGH,CRITICAL \
                          --format json \
                          --output /results/expense-frontend.json \
                          192.168.56.30:8082/expense-frontend:latest
                        '''
                    }
                }
            }

//         stage('Stop Security VM') {
//             steps {
//                 sh '''
//                 ssh -i /var/jenkins_home/.ssh/id_rsa_vmjenkins_nopass -o StrictHostKeyChecking=no mehdi@192.168.1.15 << 'EOF'
// STATE=$(VBoxManage showvminfo securite --machinereadable | grep VMState=)
// if echo $STATE | grep -q running; then
//     echo 'Stopping Security VM'
//     VBoxManage controlvm securite acpipowerbutton
// else
//     echo 'Security VM already stopped'
// fi
// EOF
//                 '''
//             }
//         }
    
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