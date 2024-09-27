pipeline {
    agent any
    
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub_login')
        GITHUB_CREDENTIALS = credentials('github_login')
        GITHUB_TOKEN = credentials('github_token')
        DOCKERHUB_REPO = 'mad1011/query-exporter-app'
    }

    stages {
        stage ("Clone project") {
          steps {
            git branch: 'main', url: 'https://github.com/hadam1011/Query-exporter-app.git'
          }
        }

        stage('SonarCloud analysis') {
            environment {
                scannerHome = tool 'Sonarqube scanner'
            }

            steps {
                powershell 'Copy-Item -Path "springboot-backend/pom-analysis.xml" -Destination "springboot-backend/pom.xml" -Force'

                withSonarQubeEnv(credentialsId: 'sonarcloud_token', installationName: 'SonarCloud') {
                    dir ('./springboot-backend') {
                        withMaven {
                            bat 'mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=hadam1011_Query-exporter-app'
                        }
                    }
                    dir ('./react-frontend') {
                        bat '''
                            ${scannerHome}\\bin\\sonar-scanner \
                            -D sonar.projectKey=hadam1011_Query-exporter-app \
                            -D sonar.projectName=Query-exporter-app \
                            -D sonar.sources=./src \
                        '''
                    }
                }
            }
        }

        stage ('Build images') {
            steps {
                powershell 'Copy-Item -Path "springboot-backend/pom-build.xml" -Destination "springboot-backend/pom.xml" -Force'
                dir ('./react-frontend') {
                    bat "docker build -t ${DOCKERHUB_REPO}:frontend-${BUILD_NUMBER} ."
                }
                dir ('./springboot-backend') {
                    bat "docker build -t ${DOCKERHUB_REPO}:backend-${BUILD_NUMBER} ."
                }
            }
        }

        stage ('Push images to Docker Hub') {
            steps {
                bat """
                    docker login -u ${DOCKERHUB_CREDENTIALS_USR} -p ${DOCKERHUB_CREDENTIALS_PSW}
                    docker push ${DOCKERHUB_REPO}:frontend-${BUILD_NUMBER}
                    docker push ${DOCKERHUB_REPO}:backend-${BUILD_NUMBER}
                """
            }
        }

        stage ('Update deployment files') {
            steps {
                bat """
                    git config user.email "hadam8910@gmail.com"
                    git config user.name "hadam1011"
                    powershell -Command "(Get-Content deployments/backend-deployment.yaml) -replace 'imageVersion', ${BUILD_NUMBER} | Out-File -encoding ASCII deployments/backend-deployment.yaml"
                    powershell -Command "(Get-Content deployments/frontend-deployment.yaml) -replace 'imageVersion', ${BUILD_NUMBER} | Out-File -encoding ASCII deployments/frontend-deployment.yaml"
                    
                    git clone https://github.com/hadam1011/manifests.git
                    copy deployments\\backend-deployment.yaml manifests\\query-exporter-app\\backend-deployment.yaml
                    copy deployments\\frontend-deployment.yaml manifests\\query-exporter-app\\frontend-deployment.yaml

                    cd manifests
                    git config user.email "hadam8910@gmail.com"
                    git config user.name "hadam1011"
                    git add .
                    git commit -m "Update deployment image to version ${BUILD_NUMBER}"
                    git push https://${GITHUB_TOKEN}@github.com/hadam1011/manifests
                """
            }
        }
    }
}