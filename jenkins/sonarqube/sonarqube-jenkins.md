### Sonaqube with jenkins

### On Sonarqube
* My Account ~ Security
    * Name : for-jenkins
    * Type : User Token

### On Jenkins
* Plugin : **Sonarqube Scanner**
* Jenkins ~ Manage Jenkins ~ Credentails ~ System ~ Global Credentails
    * **Create New Credentails**
        * Kind : Secret text
        * Scope : Global (Jenkins, nodes, items, all child items, etc) = default
        * Secret : ( got from Sonarqube )
        * Id : SONARQUBE_TOKEN ( it so important )
* Jenkins ~ Manage Jenkins ~ Tools
    * **Sonarqube Scanner** 
        * Name : sonarqube-scanner
        * Version : You can choose
* Jenkins ~ Manage Jenkins ~ System
    * **SonarQube installations** 
        * Name : sonarqube-scanner
        * Server URL : https://sonarqube.bunpanin.online
        * Server authentication token : SONARQUBE_TOKEN ( You have created)
* Create New Pipeline : **reactjs-sonarqube-pipeline**
