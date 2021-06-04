#!/usr/bin/env bash

# mvn clean package spring-boot:repackage

gnome-terminal -e "java -jar target/topics-0.0.1-SNAPSHOT.jar --own=1500 --left=3000 --right=2000 --topics.in=a,b,c --topics.out=a,b,c --name=Andrew --is-first=true"
gnome-terminal -e "java -jar target/topics-0.0.1-SNAPSHOT.jar --own=2000 --left=1500 --right=3000 --topics.in=b,c,a --topics.out=a,c,b --name=John"
gnome-terminal -e "java -jar target/topics-0.0.1-SNAPSHOT.jar --own=3000 --left=2000 --right=1500 --topics.in=a,c,b --topics.out=a,c,b --name=Karl"
# gnome-terminal -e 'java -jar target/topics-0.0.1-SNAPSHOT.jar --own=4000 --left=3000 --right=1000 --topics=a,c --name=Max'