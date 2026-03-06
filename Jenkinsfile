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

        // stage('Start Security VM') {
        //     steps {
        //         sh '''
        //             set -x
        //             ssh -T -i /var/jenkins_home/.ssh/id_rsa_vmjenkins_nopass -o StrictHostKeyChecking=no mehdi@192.168.1.15 '
        //             STATE=$(VBoxManage showvminfo securite --machinereadable | grep VMState=)
        //             if echo "$STATE" | grep -q poweroff; then
        //                 echo "Starting Security VM"
        //                 VBoxManage startvm securite --type headless
        //                 sleep 15
        //             else
        //                 echo "Security VM already running"
        //             fi
        //             '
        //         '''
        //     }
        // }

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

        // stage('Build and Push Docker Images to Nexus') {
        //     agent { label 'security' }

        //     steps {

        //         git branch: 'main', credentialsId: 'Git tok en', url: 'https://github.com/mehdibha09/Expense_Tracker.git'

        //         withCredentials([usernamePassword(
        //             credentialsId: 'nexus-creds',
        //             usernameVariable: 'NEXUS_USER',
        //             passwordVariable: 'NEXUS_PASSWORD'
        //         )]) {

        //             sh '''
        //                 set -x
        //                 IMAGE_TAG=${BUILD_NUMBER}

        //                 echo $NEXUS_PASSWORD | docker login 192.168.56.30 -u $NEXUS_USER --password-stdin

        //                 docker build -t my-nexus-repo/expense-backend:${IMAGE_TAG} -t my-nexus-repo/expense-backend:latest expense-tracker-service
        //                 docker build -t my-nexus-repo/expense-frontend:${IMAGE_TAG} -t my-nexus-repo/expense-frontend:latest expense-tracker-ui

        //                 docker tag my-nexus-repo/expense-backend:${IMAGE_TAG} 192.168.56.30/expense-backend:${IMAGE_TAG}
        //                 docker tag my-nexus-repo/expense-frontend:${IMAGE_TAG} 192.168.56.30/expense-frontend:${IMAGE_TAG}

        //                 docker tag my-nexus-repo/expense-backend:latest 192.168.56.30/expense-backend:latest
        //                 docker tag my-nexus-repo/expense-frontend:latest 192.168.56.30/expense-frontend:latest

        //                 docker push 192.168.56.30/expense-backend:${IMAGE_TAG}
        //                 docker push 192.168.56.30/expense-frontend:${IMAGE_TAG}
        //                 docker push 192.168.56.30/expense-backend:latest
        //                 docker push 192.168.56.30/expense-frontend:latest
        //             '''

        //         }
        //     }
        // }

        // stage('Create DBs') {
        //     agent { label 'k8s-agent' }

        //     environment {
        //         DB_HOST = '192.168.56.40'
        //     }

        //     steps {

        //         withCredentials([usernamePassword(
        //             credentialsId: 'db-creds',
        //             usernameVariable: 'DB_USER',
        //             passwordVariable: 'DB_PASSWORD'
        //         )]) {

        //             script {

        //                 Set PGPASSWORD environment variable

        //                 withEnv(["PGPASSWORD=${DB_PASSWORD}"]) {

        //                     def databases = ['auth_db', 'order_db', 'product_db', 'inventory_db']

        //                     databases.each { dbName ->

        //                         sh """
        //                             psql -h ${DB_HOST} -U ${DB_USER} -d postgres -tc "SELECT 1 FROM pg_database WHERE datname = '${dbName}'" | grep -q 1 || \
        //                             psql -h ${DB_HOST} -U ${DB_USER} -d postgres -c "CREATE DATABASE ${dbName};"
        //                         """

        //                     }

        //                 }

        //             }

        //         }

        //     }
        // }

        // stage('Security Scan') {
        //     agent { label 'security' }

        //     steps {

        //         withCredentials([usernamePassword(
        //             credentialsId: 'nexus-creds',
        //             usernameVariable: 'NEXUS_USER',
        //             passwordVariable: 'NEXUS_PASSWORD'
        //         )]) {

        //             sh '''
        //                 set -x

        //                 echo $NEXUS_PASSWORD | docker login 192.168.56.30 -u $NEXUS_USER --password-stdin

        //                 docker run --rm \
        //                     -v /var/run/docker.sock:/var/run/docker.sock \
        //                     -v /opt/trivy-cache:/root/.cache/trivy \
        //                     -v /mnt/nfs/trivy/results:/results \
        //                     aquasec/trivy image \
        //                     --severity HIGH,CRITICAL \
        //                     --format json \
        //                     --output /results/expense-backend.json \
        //                     192.168.56.30/expense-backend:latest

        //                 docker run --rm \
        //                     -v /var/run/docker.sock:/var/run/docker.sock \
        //                     -v /opt/trivy-cache:/root/.cache/trivy \
        //                     -v /mnt/nfs/trivy/results:/results \
        //                     aquasec/trivy image \
        //                     --severity HIGH,CRITICAL \
        //                     --format template \
        //                     --template "@/contrib/html.tpl" \
        //                     --output /results/expense-backend.html \
        //                     192.168.56.30/expense-backend:latest

        //                 docker run --rm \
        //                     -v /var/run/docker.sock:/var/run/docker.sock \
        //                     -v /opt/trivy-cache:/root/.cache/trivy \
        //                     -v /mnt/nfs/trivy/results:/results \
        //                     aquasec/trivy image \
        //                     --severity HIGH,CRITICAL \
        //                     --format json \
        //                     --output /results/expense-frontend.json \
        //                     192.168.56.30/expense-frontend:latest

        //                 docker run --rm \
        //                     -v /var/run/docker.sock:/var/run/docker.sock \
        //                     -v /opt/trivy-cache:/root/.cache/trivy \
        //                     -v /mnt/nfs/trivy/results:/results \
        //                     aquasec/trivy image \
        //                     --severity HIGH,CRITICAL \
        //                     --format template \
        //                     --template "@/contrib/html.tpl" \
        //                     --output /results/expense-frontend.html \
        //                     192.168.56.30/expense-frontend:latest
        //             '''

        //         }

        //     }
        // }

        // stage('Publish Security Reports trivey') {
        //     agent { label 'security' }

        //     steps {

        //         sh '''
        //             set -x

        //             mkdir -p reports/trivy reports/zap

        //             cp -f /mnt/nfs/trivy/results/expense-backend.json reports/trivy/ 2>/dev/null || true
        //             cp -f /mnt/nfs/trivy/results/expense-frontend.json reports/trivy/ 2>/dev/null || true
        //             cp -f /mnt/nfs/trivy/results/expense-backend.html reports/trivy/ 2>/dev/null || true
        //             cp -f /mnt/nfs/trivy/results/expense-frontend.html reports/trivy/ 2>/dev/null || true
        //         '''

        //         archiveArtifacts artifacts: 'reports/**/*.html, reports/**/*.json', allowEmptyArchive: true

        //         publishHTML(target: [
        //             allowMissing: true,
        //             alwaysLinkToLastBuild: true,
        //             keepAll: true,
        //             reportDir: 'reports/trivy',
        //             reportFiles: 'expense-backend.html',
        //             reportName: 'Trivy Backend Report'
        //         ])

        //         publishHTML(target: [
        //             allowMissing: true,
        //             alwaysLinkToLastBuild: true,
        //             keepAll: true,
        //             reportDir: 'reports/trivy',
        //             reportFiles: 'expense-frontend.html',
        //             reportName: 'Trivy Frontend Report'
        //         ])

        //     }
        // }

        // stage('Deploy to Kubernetes') {
        //     agent { label 'k8s-agent' }

        //     steps {

        //         git branch: 'main', credentialsId: 'Git tok en', url: 'https://github.com/mehdibha09/Expense_Tracker.git'

        //         withCredentials([usernamePassword(
        //             credentialsId: 'nexus-creds',
        //             usernameVariable: 'NEXUS_USER',
        //             passwordVariable: 'NEXUS_PASSWORD'
        //         )]) {

        //             dir('k8s') {

        //                 sh '''
        //                     set -x

        //                     IMAGE_TAG=${BUILD_NUMBER}

        //                     kubectl apply -f namespace.yaml

        //                     kubectl -n expense-tracker create secret docker-registry nexus-regcred \
        //                         --docker-server=192.168.56.30 \
        //                         --docker-username=$NEXUS_USER \
        //                         --docker-password=$NEXUS_PASSWORD \
        //                         --docker-email=devnull@example.com \
        //                         --dry-run=client -o yaml | kubectl apply -f -

        //                     kubectl apply -f backend-deployment.yaml
        //                     kubectl apply -f backend-service.yaml
        //                     kubectl apply -f frontend-deployment.yaml
        //                     kubectl apply -f frontend-service.yaml

        //                     kubectl -n expense-tracker set image deployment/expense-backend expense-backend=192.168.56.30/expense-backend:${IMAGE_TAG}
        //                     kubectl -n expense-tracker set image deployment/expense-frontend expense-frontend=192.168.56.30/expense-frontend:${IMAGE_TAG}

        //                     kubectl -n expense-tracker rollout status deployment/expense-backend --timeout=180s
        //                     kubectl -n expense-tracker rollout status deployment/expense-frontend --timeout=180s
        //                 '''

        //             }

        //         }

        //     }
        // }

        stage('OWASP ZAP Full Scan') {
            agent { label 'security' }

            steps {

                script {

                    int zapExitCode = sh(
                        script: """
                            set -x

                            docker run --rm \
                                --name owasp-zap-scan-${BUILD_NUMBER} \
                                --cpus="0.7" \
                                -v /mnt/nfs/owasp-zap:/zap/wrk \
                                ghcr.io/zaproxy/zaproxy:stable \
                                zap-full-scan.py \
                                -t http://192.168.56.10:30080 \
                                -J /zap/wrk/zap-report-${BUILD_NUMBER}.json \
                                -r /zap/wrk/zap-report-${BUILD_NUMBER}.html
                        """,
                        returnStatus: true
                    )

                    if (zapExitCode == 0) {
                        echo 'OWASP ZAP completed successfully (exit code 0).'
                    } else if(zapExitCode == 2) {
                        echo 'OWASP ZAP exited with code 2. There are vulnerabilities found.'
                    } else if (zapExitCode == 3) {
                        echo 'OWASP ZAP exited with code 3.Medium or high risk vulnerabilities were found.'
                    } else {
                        error "OWASP ZAP scan failed with exit code ${zapExitCode}"
                    }

                }

            }
        }

        stage('Publish Security Reports owasp zap') {
            agent { label 'security' }

            steps {

                sh '''
                    set -x

                    cp -f /mnt/nfs/owasp-zap/zap-report-${BUILD_NUMBER}.html reports/zap/ 2>/dev/null || true
                    cp -f /mnt/nfs/owasp-zap/zap-report-${BUILD_NUMBER}.json reports/zap/ 2>/dev/null || true
                '''

                archiveArtifacts artifacts: 'reports/**/*.html, reports/**/*.json', allowEmptyArchive: true

                publishHTML(target: [
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'reports/zap',
                    reportFiles: "zap-report-${BUILD_NUMBER}.html",
                    reportName: 'OWASP ZAP Report'
                ])

            }
        }

        // stage('Stop Security VM') {
        //     steps {

        //         sh '''
        //             ssh -T -i /var/jenkins_home/.ssh/id_rsa_vmjenkins_nopass -o StrictHostKeyChecking=no mehdi@192.168.1.15 '

        //             STATE=$(VBoxManage showvminfo securite --machinereadable | grep VMState=)

        //             if echo "$STATE" | grep -q running; then
        //                 echo "Stopping Security VM"
        //                 VBoxManage controlvm securite acpipowerbutton
        //             else
        //                 echo "Security VM already stopped"
        //             fi

        //             '
        //         '''

        //     }
        // }

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