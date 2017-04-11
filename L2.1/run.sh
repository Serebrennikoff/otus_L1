#!/bin/sh
mvn clean compiler:compile
java -XX:InitialHeapSize=512m -XX:MaxHeapSize=512m -cp ./target/classes/ ru.otus.l2.Main
