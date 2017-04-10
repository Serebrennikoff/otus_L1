#!/bin/sh
mvn clean compiler:compile
jar -cvfm target/Agent.jar manifest.mf target/classes/ru/otus/agent/Agent.class
java -javaagent:target/Agent.jar -cp ./target/classes/ ru.otus.main.Main