#!/usr/bin/env groovy

def call(String name) {
    pipeline {
        agent any
            stages {
                stage ('SCM') {
                    steps {
                        echo "SCM URL, ${name}."
                        git "${name}";
                    }
                    
                }
               stage ('Build') {
                    steps {
                        sh 'mvn clean package';
                    }
                    
                } 
            }
        }
    }