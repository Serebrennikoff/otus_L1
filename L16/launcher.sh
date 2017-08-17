#!/usr/bin/env bash

PATH_TO_PROJECT=/c/Users/User/IdeaProjects/L1/L16

mvn -f ${PATH_TO_PROJECT}/message-system/pom.xml clean install
mvn -f ${PATH_TO_PROJECT}/db-service/pom.xml clean package
mvn -f ${PATH_TO_PROJECT}/frontend/pom.xml clean package

cp ${PATH_TO_PROJECT}/frontend/target/root.war ${PATH_TO_PROJECT}/jetty-server/base/webapps/

java -jar ${PATH_TO_PROJECT}/message-system/target/message-system.jar