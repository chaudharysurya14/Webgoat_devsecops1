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
    stage ('Send jar file to app_server') {
            steps {
           sshagent(['application_server']) {
                sh 'scp -o StrictHostKeyChecking=no /var/lib/jenkins/workspace/pipeline_webgoat_devsecops/target/webgoat-server-v8.2.0-SNAPSHOT.jar ubuntu@52.66.211.223:~/WebGoat'
           } 
            }
    }
  }  
}
