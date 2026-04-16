pipeline {
    agent any

    parameters {
        choice(name: 'ENV', choices: ['qa', 'dev', 'prod'], description: 'Environment')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser')
        string(name: 'THREADS', defaultValue: '2', description: 'Threads')
    }

    stages {

        stage('Checkout') {
            steps {
                git 'https://your-repo-url.git'
            }
        }

        stage('Start Selenium Grid') {
            steps {
                echo "Starting Selenium Grid..."
                sh 'docker-compose up -d'
            }
        }

        stage('Wait for Grid') {
            steps {
                script {
                    echo "⏳ Waiting for Grid to be ready..."

                    // Wait until Grid is up
                    sh '''
                    for i in {1..10}
                    do
                      curl -s http://localhost:4444/status | grep "ready" && break
                      echo "Waiting for Grid..."
                      sleep 5
                    done
                    '''
                }
            }
        }

        stage('Run Tests') {
            steps {
                echo "Running Tests..."

                sh """
                mvn clean test \
                -Denv=${ENV} \
                -Dbrowser=${BROWSER} \
                -Dexecution=remote \
                -DgridUrl=http://localhost:4444/wd/hub \
                -Dthreads=${THREADS}
                """
            }
        }

        stage('Allure Report') {
            steps {
                echo "Generating Allure Report..."
                allure results: [[path: 'target/allure-results']]
            }
        }
    }

    post {
        always {
            echo "Stopping Selenium Grid..."
            sh 'docker-compose down'
        }

        success {
            echo "Build Successful"
        }

        failure {
            echo "Build Failed"
        }
    }
}