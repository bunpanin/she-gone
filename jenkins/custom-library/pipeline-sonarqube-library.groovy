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
    }
}