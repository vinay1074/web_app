#!/usr/bin/env groovy

def call(String name) {
    pipeline {
        agent any
        {
            stages {
                stage {
                    steps ('SCM') {
                        echo "SCM URL, ${name}."
                    }
                }
            }
        }
    }
  

}

