## Nexus

Containerization 
Docker ( Dockerfile, Docker image, Container , Network , Volume , ...)

- We use docker image in order to run to containers 


### Container Registry 
> Repository used for storing the images ( docker images )
- Dockerhub , registry.gitlab.com , ghcr.io 

-> **Nexus OSS Repository** : used in order to store docker images 
Self-Hosted 
- Docker Image registry
- Helm Registry

*** 

```bash
# To access the web UI 
http://35.213.173.163:8081 - nexus-cloud.bunpanin.online

# For docker image , we will use 
http://35.213.173.163:5000 - nexus-cr.bunpanin.online
```


### Blob Store
```bash
- Type : File
- Name : docker-hosted-blob
```
### Repositories
```bash
- docker(hosted)
- Name : docker-hosted
- Http : 5000
- Allow Anonymouse docker full
- Enable docker v1 Api
- Blob Store ( Choose your blob your i have create )
- Deployment Policy
```
### Realms
```bash
- Docker Bearer Token ( chhose Above )
```

### Login To Pushing the docker image
```bash
docker login -u admin nexus-cr.bunpanin.online

docker pull nginx 
docker tag nginx:latest nexus-cr.bunpanin.online/nginx-nexus:v1.0.0 
docker push  nexus-cr.bunpanin.online/nginx-nexus:v1.0.0
```

### Create Privilage for User Pull Image
```bash
+ Role
    - Role Type : Nexus Role
    - Role Id : pull-docker-only
    - Role Name : pull-docker-only
    - Role Description : Used for only pulling the docekr images
    - Applies Privilege
        * nx-repository-view-docker-*-read
+ User
    - Add Your Role Create
```

### User Pull Image
```bash
# If Your already login
docker logout nexus-cr.bunpanin.online
# Need to login with your user pull-images-only
docker login nexus-cr.bunpanin.online
# You are already login
docker pull nexus-cr.bunpanin.online/hello-world:v2.0.0
```