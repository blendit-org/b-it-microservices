FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY target/scoringSystem-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8006

ENTRYPOINT [ "java", "-jar", "app.jar" ]