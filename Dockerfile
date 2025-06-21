FROM openjdk:21
WORKDIR /app
COPY target/notifyme-*.jar notifyme.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "notifyme.jar"]
