#!/usr/bin/env bash

PATH_TO_PROJECT=/c/Users/User/IdeaProjects/L1/L16
JETTY_HOME=C:/jetty-distribution-9.4.6.v20170531
PORT=8091
#PORT=8092

cd ${PATH_TO_PROJECT}/jetty-server/base

java -jar ${JETTY_HOME}/start.jar jetty.http.port=${PORT}