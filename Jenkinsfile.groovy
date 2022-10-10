node('master01'){
    stage('SCM') {
        sh '''rm -r web_app
        git clone https://github.com/vinay1074/web_app.git
        '''
    }
    stage('Test'){
        withSonarQubeEnv('SonarQube'){
        sh '''cd web_app
        mvn clean verify sonar:sonar \
  -Dsonar.projectKey=web-app-anal \
  -Dsonar.host.url=http://10.0.1.122:9000 \
  -Dsonar.login=sqp_5e46e4085c7730d7a6d853890b9e1fc597f1a11c
  '''
    }
    }
    stage("Quality gate") {
             timeout(time: 2, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
    stage('Build') {
        sh '''cd web_app
        mvn clean package
        docker build -t web_app .
        docker tag web_app vinay1074/web_app
        docker push vinay1074/web_app
        docker rmi web_app vinay1074/web_app
        '''
    }
    stage('K8S Deploy') {
        sh '''cd web_app
        kubectl apply -f Kubernetes_Deployment.yml
        kubectl apply -f Kubernetes_Service.yml
        '''
    }

}