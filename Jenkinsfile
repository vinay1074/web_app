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
  -Dsonar.projectKey=web-app-anal 
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
        stage("Deploy to tomcat")
        {
            steps{
                sh '''sudo ansible-playbook tomcat_deploy.yml'''
            }
        }
}
}