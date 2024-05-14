pipeline {
    agent any

    tools {
        maven 'my-maven'
    }
    environment {
        MYSQL_ROOT_LOGIN = credentials('mysql-root-login')
    }
    stages {
        stage('Build project with Maven') {
            steps {
                sh 'mvn --version'
                sh 'java --version'
                sh 'mvn clean package -Dmaven.test.failure.ignore=true'
            }
        }

        stage('Packaging | Pushing images') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
                    sh 'docker build -t nguyenduc1409/saa:1.0.0 -f deployment/docker/Dockerfile .'
                    sh 'docker push nguyenduc1409/saa:1.0.0'
                }
            }
        }

        stage('Deploy MySQL') {
            steps {
                echo 'Deploying'
                sh 'docker image pull mysql:mysql-server'
                sh 'docker network create spring-demo || echo "this network exists"'
                sh 'docker container stop mysql-server-saa || echo "this container does not exist"'
                sh 'echo y | docker container prune'
                // sh 'docker volume rm mysql-server-data || echo "no volume"'

                sh 'docker run --name mysql-server-saa --rm --network spring-demo -e MYSQL_ROOT_PASSWORD=${MY_ROOT_LOGIN_PSW} -e MYSQL_DATABASE=authenticate_db -d mysql:mysql-server'
                sh 'sleep 20'
            }
        }
    }

    //post  {
    //    always {
    //        cleanWs()
    //    }
    //}
}