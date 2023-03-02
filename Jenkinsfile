pipeline {
  agent any 
  stages {
    stage ('Initialize') {
      steps {
        sh '''
                echo "PATH = ${PATH}"
                echo "M2_HOME = ${M2_HOME}"
            ''' 
      }
    }
    
    stage ('Check secrets') {
      steps {
      sh 'trufflehog3 https://github.com/chaudharysurya14/Webgoat_devsecops1.git -f json -o truffelhog_output.json || true'
      }
    }
    
    stage ('Software composition analysis') {
            steps {
                dependencyCheck additionalArguments: ''' 
                    -o "./" 
                    -s "./"
                    -f "ALL" 
                    --prettyPrint''', odcInstallation: 'DP-Check'

                dependencyCheckPublisher pattern: 'dependency-check-report.xml'
            }
        }
    
    stage ('Static analysis') {
      steps {
        withSonarQubeEnv('sonarqube') {
          sh 'mvn sonar:sonar'
        //sh 'sudo python3 Devsecops.py'
	}
      }
    }
    stage('Build') {

            **dir('project-dir') {**
                sh 'mvn clean install'

                def pom = readMavenPom file:'pom.xml'

                print pom.version
                env.version = pom.version
            }
    }
  }
}
