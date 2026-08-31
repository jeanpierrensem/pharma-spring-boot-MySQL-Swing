pipeline {
    agent {
        //dockerContainer {
        //    image 'maven:3.9.16-eclipse-temurin-21-alpine'
       // }
    }

     // options {
      //    timestamps()
       //   buildDiscarder(logRotator(numToKeepStr: '20'))
      //    timeout(time: 30, unit: 'MINUTES')
        // disableConcurrentBuilds()
     // }

    //environment {
        // Les tests tournent sur H2 en mémoire (voir src/test/resources/application-test.properties),
        // aucune base MySQL n'est nécessaire pour ce pipeline.
     //   SPRING_PROFILES_ACTIVE = 'test'
    //}

    stages {

    stage('Hello') {
       echo 'Hello World'
    }

    }

}
