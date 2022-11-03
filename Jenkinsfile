pipeline {
    agent {
        label "master01"
    }
    tools {
        maven "Maven"
    }
        environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "10.0.4.241:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIAL_ID = "nexus-user-credentials"
        }
    stages {
        stage("Clone code from VCS") {
            steps {
                script {
                    git 'https://github.com/vinay1074/web_app.git';
                }
            }
        }
        stage("Test"){
            steps{
                withSonarQubeEnv('SonarQube'){
        sh '''mvn clean verify sonar:sonar \
  -Dsonar.projectKey=web-app-analysis 
  '''
    }
            }
        
    }
    stage("Quality gate") {
             steps{
                timeout(time: 2, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
             }
    }
    stage("Build") {
        steps{
            sh 'mvn clean deploy'
              }
    }
       
    stage('Push to Nexus'){
           steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path;
                    artifactExists = fileExists artifactPath;
                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                        nexusArtifactUploader(nexusVersion: NEXUS_VERSION, protocol: NEXUS_PROTOCOL, nexusUrl: NEXUS_URL, groupId: pom.groupId, version: pom.version, repository: NEXUS_REPOSITORY, credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId, classifier: '', file: artifactPath, type: pom.packaging],
                                [artifactId: pom.artifactId, classifier: '', file: "pom.xml", type: "pom"]
                            ]);} 
                        else {
                               error "*** File: ${artifactPath}, could not be found";
                    }
               }  
           }  
        }
           stage ('Docker Build') {
                steps {
         // Build and push image with Jenkins' docker-plugin
                  sh label: '', script: '''aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 453411705158.dkr.ecr.us-east-1.amazonaws.com
                  docker build -t web_app .
                  docker tag web_app 453411705158.dkr.ecr.us-east-1.amazonaws.com/web_app
                  docker push 453411705158.dkr.ecr.us-east-1.amazonaws.com/web_app
                  '''

                  
            
            }
        }

        stage("Deploy to tomcat")
        {
            steps{
                sh '''sudo ansible-playbook tomcat1_deploy.yml --vault-password-file /root/passwd.txt'''
            }
        }
}
}

