#!/usr/bin/env groovy

class mvn{
    String url
    String branch
    
    pipeline{
    agent any
    stages{
        stage ('GITSCM'){
            steps{
               echo "${mvn.url}" 
               echo "${mvn.branch}"
              git 'url';        
             }
        }
        stage('Build'){
            steps{
                sh 'mvn clean package'
            }
        }
    }
}

}