[![Build Status](https://travis-ci.com/sha1n/spring-boot-jetty-jersey.svg?branch=master)](https://travis-ci.com/sha1n/spring-boot-jetty-jersey)

spring-boot-jetty-jersey
========================
This repository contains a simple example of how to build a Spring-Boot based executable web application using an embedded Jetty and Jersey 2 JAX-RS implementation.

# How to run this example:
```
# clone
git clone https://github.com/sha1n/spring-boot-jetty-jersey.git

# build
cd spring-boot-jetty-jersey
mvn install

# run
$JAVA_HOME/bin/java -jar ./app/target/app-1.0-SNAPSHOT.jar

# test
curl -v  http://localhost:8080/api/test
```
