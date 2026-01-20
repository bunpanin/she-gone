pipeline {
    agent any
    tools{
        nodejs "node-lts"
    }
    // Im using NodeJS 18.20.2
    // Jest ( Testing framework for Frontend )
    stages {
        stage('Clone Code') {
            steps {
                git branch: 'main', url: 'https://github.com/bunpanin/reactjs-template'
            }
        } 
        stage("Run Test"){
            steps{
                sh """
                    npm install --force
                    npm test 
                """
            }
        }
        // stage("Build"){
        //     steps{
        //         sh """
        //             docker build -t jenkins-react-pipeline . 
        //         """
        //     }
        // }

        // Build With Version Tag for Dockerhub
        stage("Build"){
            steps{
                sh """
                    docker build -t paninbaychar/jenkins-react-pipeline:${env.BUILD_NUMBER} . 
                """
            }
        }
        // Push to Dockerhub after Build
        stage("Push to Dockerhub"){
            steps{
                withCredentials([usernamePassword(credentialsId: 'DOCKERHUB', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                    sh """
                    echo '$PASSWORD' |  docker login -u $USERNAME --password-stdin   
                    docker push paninbaychar/jenkins-react-pipeline:${env.BUILD_NUMBER} 
                    """
                }
            }
        }
        
        // stage("Deploy"){
        //     steps{
        //         sh"""
        //         docker stop reactjs-cont || true 
        //         docker rm reactjs-cont || true 


        //         docker run -dp 3000:8080 \
        //             --name reactjs-cont \
        //             jenkins-react-pipeline
        //         """
        //     }
        // }

        // Deploy from Dockerhub Image
        stage("Deploy"){
            steps{
                sh"""
                docker stop reactjs-cont || true 
                docker rm reactjs-cont || true 

                docker run -dp 3000:8080 \
                    --name reactjs-cont \
                    paninbaychar/jenkins-react-pipeline:${env.BUILD_NUMBER}
                """
            }
        }

        // stage("Push to Dockerhub "){
        //     steps{
        //             withCredentials([usernamePassword(credentialsId: 'DOCKERHUB', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
    
        //               sh """
        //             echo '$PASSWORD' |  docker login -u $USERNAME --password-stdin   
        //         docker push panin/jenkins-react-pipeline:${env.BUILD_NUMBER} 
        //         """
        //             }

                
        //     }
        // }

        // stage("Deploy"){
        //     steps{
        //         sh"""
        //         docker stop reactjs-cont || true 
        //         docker rm reactjs-cont || true 


        //         docker run -dp 3000:80 \
        //             --name reactjs-cont \
        //             lyvanna544/jenkins-react-pipeline
        //         """

        //     }
        // }

        stage("Add Domain Name"){
            steps{
                sh """

                which certbot
                certbot --version 
                # write reverse proxy config 
                # sudo nginx -s reload 
                """ 
            }
        }


    }
}
