#!/usr/bin/env bash

MEMORY="-Xms512m -Xmx512m -XX:MaxMetaspaceSize=256m"
GC="-XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark -XX:+UseParNewGC"
JMX="-Dcom.sun.management.jmxremote.port=15025 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
DUMP="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./dumps/"
SERIAL_GC="-XX:+UseSerialGC"
PARAL_GC="-XX:+UseParallelOldGC"
G1="-XX:+UseG1GC"

mvn package
java $MEMORY $G1 $DUMP $JMX -XX:OnOutOfMemoryError="kill -3 %p" -jar target/Lesson_4.jar > ./logs/gc_performance.log