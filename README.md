Example Authentication Service
==============================

This simple service demonstrates how to handle user management and authentication with Spring Boot, JWT and H2-DB.

Build
-----

The project builds with maven.

`./mvnw clean package`

Run
---

The build produces an executable jar. It can be started with `java -DjwtSignKey=secretkey -jar target/authentication-0.0.1-SNAPSHOT.jar`.

*secretkey* needs to match the secret key used in the authenticated service.

Docker
------

A docker image can be build with `docker build --tag authservice .`

The jwt signing key needs to be specified when starting the container as an environment variable `JWTSIGNINGKEY`.

`docker run -e JWTSIGNINGKEY=secretkey authservice`