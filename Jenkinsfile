pipeline {
    agent any
    environment {
        DOCKER_IMAGE_NAME = "grocamador/emealab-cicd"
        DOCKERHUB_CREDENTIALS= credentials('dockerhubcredentials')
        }
    
stages {

    stage('Login ECR') {
            when {
                branch 'main'
            }
        steps {
                echo 'Loging to ECR'
                sh "aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 059797578166.dkr.ecr.eu-central-1.amazonaws.com"
            }
        }
    stage('Build Docker Image') {
            when {
                branch 'main'
            }
        steps {
                echo 'Building docker image'
                sh "docker build -t ${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER} ."
            }
        }

 
      stage('Scanning Image with Sysdig') {
	     when {
                branch 'main'
            }
        steps {
            
            sh "echo ${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER} > sysdig_secure_images"
            script {
            try {
            sysdigImageScan engineCredentialsId: 'sysdig-api-emealab', imageName: "${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}"
            }
            catch (Exception e) {
                            input "Sysdig Vulnerability scanner showed some security issues, Are you sure you want to continue?"  
                        }
                    }
        }
       } 

    stage('Push Docker Image') {
        when {
            branch 'main'
        }
        steps {

                echo "Login in docker registry"
                sh "docker login -u $DOCKERHUB_CREDENTIALS_USR -p $DOCKERHUB_CREDENTIALS_PSW"                		
	            echo 'Login Completed' 
                echo "Pushing docker image with current build tag"
                sh " docker push ${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER}"
                echo 'Pushing docker image with tag latest'
                sh "docker tag ${DOCKER_IMAGE_NAME}:${env.BUILD_NUMBER} ${DOCKER_IMAGE_NAME}:latest"
                sh "docker push ${DOCKER_IMAGE_NAME}:latest"
            }
        }
        
        
       stage("Deploy to Production"){
        when {
                branch 'main'
            }
             steps {              

              sh ("""                
                  kubectl delete -f account-portal.yaml                 
                  kubectl apply -f account-portal.yaml
                """)
                
             }
         }
    }
}
