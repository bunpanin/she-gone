## RUN Test - Reactjs
* go to Plugin and install **Node**
* go to Tool create your node version (example: node-lts)
* in pipeline dont forget Add **Tool**
```bash
pipeline{
    agent{
        label 'master'
    }
    tools{
        nodejs "node-lts"
    }
}

# Before Run test with react make sure your react project woring with jest package ( jest package = Run Test )
```
## Github-Webhook & and GitLab Webhook (Panin can to youtube for gitlab webhook)

* In your github have menu name **Webhook** click it 
* on Payload URL*
```bash
# Sample
https://usernamejenkins:yourTokenGenerateOnJenkins@yourdomainjenkins.com/github-webhook/

# Example
https://pn:6435435353535353@jenkins.bunpanin.online/github-webhook/

# Before get 6435435353535353 we need go to jenkins go to security create a new Token

```
* chhose Just the **push** event
* go to your project Pipeline on **Trigger** 
* on Function Trigger we chhoose or click on **GitHub hook trigger for GITScm polling**


## Push Image to dockerHub
* On DockerHub Account go setting chhose or click on **Personal Access Token**
* Create Access Token
```bash
# Write name for access token
# Choose Expiration Date
# Access Permissions -> Im chhose Read & Write
```

* On Jenkins create a New Credentails
* Manage Jenkins -> Credentails -> System -> Global Credentails
```bash
# Kind : username with password
# Scope : chhose Default
# username Add your username on Dockerhub
# Password Get from DockerHub when we Create Access Token
# ID DOCKERHUB (This id so important)
```
* Go to Your Pipeline
```bash
stage("Build"){
    steps{
        sh """
            docker build -t paninbaychar/jenkins-react-pipeline:${env.BUILD_NUMBER} . 
        """
    }
}

### Before get withCredentials
-   Go pipeline-syntax
    - choose :  withCredentials: Bind credentials to variables
    - Click add : Username and password (separated)?
        + Username Variable : USERNAME
        + Password Variable : PASSWORD
        + Credential ( from Credential you created )
### And Got This
withCredentials([usernamePassword(credentialsId: 'DOCKERHUB', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
    // some block
}



stage("Push to Dockerhub "){
    steps{
            withCredentials([usernamePassword(credentialsId: 'DOCKERHUB', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {

                sh """
            echo '$PASSWORD' |  docker login -u $USERNAME --password-stdin   
        docker push paninbaychar/jenkins-react-pipeline:${env.BUILD_NUMBER} 
        """
            }

        
    }
}

stage("Deploy"){
    steps{
        sh"""
        docker stop reactjs-cont || true 
        docker rm reactjs-cont || true 


        docker run -dp 3000:80 \
            --name reactjs-cont \
            lyvanna544/jenkins-react-pipeline
        """

    }
}
```
<div style="display: flex;>

  <img src="Screenshot from 2026-01-12 23-00-41.png"/>
  <img src="Screenshot from 2026-01-12 23-05-57.png"/>

</div>


* Go to env-vars.html - http://localhost:8080/env-vars.html


### Docker Pipeline
* need to install 2 plugin


<img src="Screenshot from 2026-01-12 23-25-38.png"/>

* Write in your pipiline
```bash
pipeline{
    agent any
    
    // additoinal configration 
    environment{
        DOCKER_IMAGE="myreactjs-image"
        DOCKER_USERNAME="james168"
    }
    // Docker pipeline ( used docker command like in programming style )
    stages{
        stage("Show ENV variable "){
            steps{
                sh """
                echo "Full Image name is : ${DOCKER_USERNAME}/${DOCKER_IMAGE}"
            """ 
            }
           
        }
        stage("Deploy Nginx Container ")
        {
            
            steps{
                sh """
                docker stop nginx-cont || true 
                docker rm nginx-cont || true 
                """

            script{
                def nginxApp = docker.image("nginx:latest")
                nginxApp.inside{
                    sh """
              
                     ls -lrt 
                     nginx -v 
                    """
                }
                nginxApp.run(" --name nginx-cont -dp 8081:80")
            }
            }
        }
    }
}
```
* add sonarqube for with it