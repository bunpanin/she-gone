@Library("my-shared-library")_

pipeline {
    agent any

    stages{ 

        stage('Clone ReactJs Code ') {
            steps {
                git branch: 'main', url: 'https://github.com/bunpanin/reactjs-template'
            }
        }
        stage("Check Code Quality in Sonarqube "){
            environment {
                scannerHome= tool 'sonarqube-scanner' 
            }
            steps{ 
                script{
                    def projectKey = 'react-pipeline-library' 
                    def projectName = 'React-Pipeline-Library'
                    def projectVersion = '1.0.0'
                    scanReactSonarqubeV2("${projectKey}","${projectName}","${projectVersion}")
                }
            }
        }
        // wait for the quality gate 
        stage("Wait for Quality Gate "){
            steps{
                script{
                   def qg = waitForQualityGate()
                    if ( qg.status != 'OK'){
                        sh """
                        echo " No need to build since you QG is failed "
                        """
                        currentBuild.result='FAILURE'
                        return 
                    }else {
                        echo "Quality of code is okay!! "
                        currentBuild.result='SUCCESS'
                    }
                }

            }
        }

        stage("Build"){
            when {
                expression { 
                    currentBuild.result == 'SUCCESS'
                }
            }
            steps{
                 sh """
                    docker build -t paninbaychar/react-library-sonarqube-pipeline:${env.BUILD_NUMBER} . 
                """
            }
        }

        stage("Push"){
            when {
                expression { 
                    currentBuild.result == 'SUCCESS'
                }
            }
            steps{
               script{
                def dockerImage="paninbaychar/react-library-sonarqube-pipeline:${env.BUILD_NUMBER}"
                 pushDockerToDH(dockerImage)
               }
            }
        }
        stage("Deploy"){
            steps{
                sh"""
                docker stop reactjs-cont || true 
                docker rm reactjs-cont || true 

                docker run -dp 3000:8080 \
                    --name reactjs-cont \
                    paninbaychar/react-library-sonarqube-pipeline:${env.BUILD_NUMBER}
                """
            }
        }
    }

    post{
        success{
                script{  
                    
                    def successMessage="""
                            Deployment is Success 👍
                            🟢 Access Service: 34.101.163.214:3000            
                            📥  Job Name: ${env.JOB_NAME}
                            🎱   Build Number: ${env.BUILD_NUMBER}
                            """ 
                    sendTelegramMessageV2(successMessage)
                }
            
        }

        failure {
            script{ 
                
                def errorMessage="""
                            Deployment result in failures 🔥
                            🟢 Access Report: https://sonarqube.devnerd.store/dashboard?id=react-pipeline-library           
                            📥  Job Name: ${env.JOB_NAME}
                            🎱   Build Number: ${env.BUILD_NUMBER}
                            """ 
                sendTelegramMessageV2(errorMessage)      
                }
        }
        always {
            echo "Clearing the workspace of ${env.JOB_NAME} "
            cleanWs()
        }
    }
}