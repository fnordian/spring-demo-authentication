FROM openjdk:11-jdk-slim
COPY . /build
RUN cd /build && ./mvnw clean package

FROM openjdk:11-jre-slim
COPY --from=0 /build/target/authentication-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar -DjwtSigningKey=$JWTSIGNINGKEY /app.jar
