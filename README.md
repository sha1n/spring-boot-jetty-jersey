[![Build Status](https://travis-ci.org/sha1n/spring-boot-jetty-jersey.svg?branch=master)](https://travis-ci.org/sha1n/spring-boot-jetty-jersey)

spring-boot-jetty-jersey
========================
This repository contains a simple example of how to build a Spring-Boot based executable web application using an embedded Jetty and Jersey 2 JAX-RS implementation.

# Main Library Versions
1. Spring-Boot 2.1.x
2. Jetty 9.4.27.v20200227
3. Jersey 2.30.1
4. Tested with open jdk 8 and 11

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
