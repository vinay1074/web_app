#!/usr/bin/env groovy

class mavn{
    String url
    String branch
    echo "${mavn.url}" 
    echo "${mavn.branch}"

    pipeline {
    agent any
    stages {
        stage ('GITSCM') {
            steps {
               echo "${mavn.url}" 
               echo "${mavn.branch}"
              git 'url';        
             }
        }
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}

}