FROM openjdk:17

WORKDIR /securityapp

COPY /target/taller6-1.0-SNAPSHOT.jar app.jar

EXPOSE 443

ENTRYPOINT ["java", "-jar", "app.jar"]