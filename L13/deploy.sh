#!/bin/sh

JETTY_BASE=/tmp/mybase

mvn clean package

cp C:/Users/User/IdeaProjects/L1/L13/target/root.war ${JETTY_BASE}/webapps/ROOT.war
