pipeline {
    agent any

    stages {
        // stage('Telegram Message') {
        //     steps {
        //         def token="8430009847:AAHmwlgAqek4TGuuXakxgZBnGsFU1NGVb0o"
        //         def chatId="5884420462"
        //         sh"""
        //         curl -s -X POST https://api.telegram.org/bot${token}/sendMessage -d chat_id="${chatId}" -d text="Hello from Jenkins !"
        //         """
        //     }
        // }
        // stage('Telegram Message') {
        //     steps {
        //         script {
        //             withCredentials([usernamePassword(credentialsId: 'TELEGRAM_BOT', passwordVariable: 'CHAT_ID', usernameVariable: 'TOKEN')]) {
        //             sh """
        //             curl -s -X POST https://api.telegram.org/bot${TOKEN}/sendMessage \
        //                 -d chat_id="${CHAT_ID}" \
        //                 -d text="Hello from Jenkins!"
        //             """
        //             }
        //         }
        //     }
        // }

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
                withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'sonarqube-scanner') {
                    script{
                        def projectKey = 'reactjs-jenkins-template' 
                        def projectName = 'ReactjsJenkinsTemplate'
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
        stage("Wait for Quality Gate"){
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
                echo "Building the docker image "
            }
        }
        stage("Push"){
            when {
                expression { 
                    currentBuild.result == 'SUCCESS'
                }
            }
            steps{
                echo "Pushing the docker image to registry "
            }
        }
        // stage("Build"){
        //     when {
        //         expression { 
        //             currentBuild.result == 'SUCCESS'
        //         }
        //     }
        //     steps{
        //          sh """
        //             docker build -t paninbaychar/jenkins-react-sonarqube-pipeline:${env.BUILD_NUMBER} . 
        //         """
        //     }
        // }
        // // wait for the quality gate 
        // stage("Wait for Quality Gate "){
        //     steps{
        //         script{
        //            def qg = waitForQualityGate()
        //             if ( qg.status != 'OK'){
        //                 sh """
        //                 echo " No need to build since you QG is failed "
        //                 """
        //                 currentBuild.result='FAILURE'
        //                 return 
        //             }else {
        //                 echo "Quality of code is okay!! "
        //                 currentBuild.result='SUCCESS'
        //                 return
        //             }
        //         }
        //     }
        // }
        // stage("Build"){
        //     when {
        //         expression {
        //             currentBuild.result == 'SUCCESS'
        //         }
        //     }
        //     steps{
        //         //  sh """
        //         //     docker build -t lyvanna544/jenkins-react-sonarqube-pipeline:${env.BUILD_NUMBER} . 
        //         // """
        //         echo " Building the docker image since QG is passed "
        //     }
        // }
     
    }
    post{
        success{
            script{
                // withCredentials([usernamePassword(credentialsId: 'TELEGRAM_BOT',
                // passwordVariable: 'TOKEN', usernameVariable: 'CHAT_ID')]) {
                        
                // sendTelegramMessage("Deployment is success! ","${TOKEN}","${CHAT_ID}")
                    
                // }     
                withCredentials([usernamePassword(credentialsId: 'TELEGRAM_BOT', passwordVariable: 'CHAT_ID', usernameVariable: 'TOKEN')]) {
                     sendTelegramMessage("Deployment is success! ","${TOKEN}","${CHAT_ID}")
                }
            }
        }
        failure {
            script{
                //     withCredentials([usernamePassword(credentialsId: 'TELEGRAM_BOT',
                //     passwordVariable: 'TOKEN', usernameVariable: 'CHAT_ID')]) {
                //     sendTelegramMessage("Deployment is Failed! ","${TOKEN}","${CHAT_ID}")
                // }     
                withCredentials([usernamePassword(credentialsId: 'TELEGRAM_BOT', passwordVariable: 'CHAT_ID', usernameVariable: 'TOKEN')]) {
                    sendTelegramMessage("Deployment is Failed! ","${TOKEN}","${CHAT_ID}")
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