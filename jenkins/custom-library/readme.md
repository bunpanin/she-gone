### Custom Shared Library 

## Our problems 
- have to declare the same function and pipeline over and over 


## Solution 
Custom shared libray , to speedup work and save up time. 



### Homework 
inside our custom library 
resources ( dockerfile ,d ocker compose for spring and react )r
- when clone code without DOckerfile or docker-compose , use resources directory in order to buiild the docker image and push it to dockerhub

> 




### How to Setup library jenkins
* **Global Trusted Pipeline Libraries**
    * Library Name : my-shared-library
    * Default version : main ( need input your branch )
    * Source Code Management : Git
    * Project Repository : https://github.com/bunpanin/shared-library
    * Fresh clone per build ( Joj Ah ng )