pipeline {
    agent any

    stages {
        stage('Clone ReactJs Code ') {
            steps {
                // git branch: 'main', url: 'https://github.com/bunpanin/reactjs-template'
                git branch: 'sambo', url: 'https://github.com/SamboDea/job_posting/tree/sambo'
            }
        }
        stage("Check Code Quality in Sonarqube "){
            environment {
                scannerHome= tool 'sonarqube-scanner' 
            }
            steps{
                withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'sonarqube-scanner') {
                    script{
                        def projectKey = 'reactjs-jobposting' 
                        def projectName = 'ReactjsJobposting'
                        def projectVersion = '1.0.0' 
                        sh """
                        ${scannerHome}/bin/sonar-scanner \
                        -Dsonar.projectKey=${projectKey} \
                        -Dsonar.projectName="${projectName}" \
                        -Dsonar.projectVersion=${projectVersion} \
                        """       
                    }
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
    }
}

// function
def sendTelegramMessage(String message, String token , String chatId) {
    // uppgrade to use Markdown version instead 
    def encodedMessage = URLEncoder.encode(message, "UTF-8")
    sh """
        curl -s -X POST https://api.telegram.org/bot${token}/sendMessage \\
        -d chat_id=${chatId} \\
        -d text="${encodedMessage}" > /dev/null
    """
}