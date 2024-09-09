pipeline {
    agent {label 'linux'}

    tools {
        jdk 'JDK-17'
        maven 'Maven 3.9.8'
    }

    stages {
        // Service Registery Module
        stage('Build Service Module') {
            steps {
                dir('ServiceRegistry') {
                    echo 'Building Service Module..'
                    sh 'mvn -v'
                    sh 'mvn clean install'
                }
            }
        }
        stage('Test Service Module') {
            steps {
                dir('ServiceRegistry') {
                    echo 'Testing Service Module..'
                    sh 'mvn test'
                }
            }
        }

        // API Gateway module
        stage('Build API gateway Module') {
            steps {
                dir('APIGateway') {
                    echo 'Building API gateway Module..'
                    sh 'mvn -v'
                    sh 'mvn clean install'
                }
            }
        }
        stage('Test API gateway Module') {
            steps {
                dir('APIGateway') {
                    echo 'Testing API gateway Module..'
                    sh 'mvn test'
                }
            }
        }

        // Recipe module
        stage('Build Recipe Module') {
            steps {
                dir('RecipeService') {
                    echo 'Building Recipe Module..'
                    sh 'mvn -v'
                    sh 'mvn clean install'
                }
            }
        }
        stage('Test Recipe Module') {
            steps {
                dir('RecipeService') {
                    echo 'Testing Recipe Module..'
                    sh 'mvn test'
                }
            }
        }

        // User module
        stage('Build User Module') {
            steps {
                dir('UserService') {
                    echo 'Building User Module..'
                    sh 'mvn -v'
                    sh 'mvn clean install'
                }
            }
        }
        stage('Test User Module') {
            steps {
                dir('UserService') {
                    echo 'Testing UM Module..'
                    sh 'mvn test'
                }
            }
        }

        // Admin module
        stage('Build Admin Module') {
            steps {
                dir('AdminService') {
                    echo 'Building Admin Module..'
                    sh 'mvn -v'
                    sh 'mvn clean install'
                }
            }
        }
        stage('Test Admin Module') {
            steps {
                dir('AdminService') {
                    echo 'Testing Admin Module..'
                    sh 'mvn test'
                }
            }
        }

        // Sonar Analysis
        stage('SonarQube analysis') {
            environment {
                scannerHome = tool 'SonarQube Scanner'
            }
            steps {
                withSonarQubeEnv('SonarHyd') {
                    sh '$scannerHome/bin/sonar-scanner -Dproject.settings=sonar-project.properties'
                }
            }
        }
    }
}