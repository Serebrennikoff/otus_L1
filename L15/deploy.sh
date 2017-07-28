#!/bin/sh

JETTY_BASE=/tmp/mybase

mvn clean package

cp C:/Users/User/IdeaProjects/L1/L15/target/root.war ${JETTY_BASE}/webapps/ROOT.war
