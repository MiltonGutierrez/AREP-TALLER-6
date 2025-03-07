FROM openjdk:17

WORKDIR /securityapp

COPY /target/taller5-1.0-SNAPSHOT.jar app.jar

EXPOSE 50001

ENTRYPOINT ["java", "-jar", "app.jar"]