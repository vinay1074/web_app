#!/usr/bin/env groovy

def call(body){
  def args = [
    branch: '',
    url: '',
  ]
body.resolveStrategy = Closure.DELIGATE_FIRST
body.delegate = args
body()
echo "INFO: ${args.branch}"

pipeline {
    agent any
    
    stages {
        stage ('git checkout'){
            steps{
                checkout([
                    $class: 'GitSCM',
                      branches: [[name: args.branch]],
                      userRemoteConfigs: [[url: args.url]]
                ])
            }
        }
        stage ('Build'){
            steps{
                sh 'mvn -Dmaven.test.failure.ignore=true install'
            }
        }

    }
}
}


