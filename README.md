[![Build Status](https://travis-ci.org/sha1n/spring-boot-jetty-jersey.svg?branch=master)](https://travis-ci.org/sha1n/spring-boot-jetty-jersey)

spring-boot-jetty-jersey
========================

This repository contains a simple example of how to build a Spring-Boot based executable web application using an embedded Jetty and Jersey 2 JAX-RS implementation.

To run this example follow these steps:

1. $ git clone https://github.com/sha1n/spring-boot-jetty-jersey.git
2. $ cd spring-boot-jetty-jersey
3. $ mvn install
4. $JAVA_HOME/bin/java -jar ./app/target/app-1.0-SNAPSHOT.jar

To test the app, send HTTP GET on http://localhost:8080/api/test
