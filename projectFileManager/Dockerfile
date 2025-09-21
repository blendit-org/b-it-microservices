FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY target/projectFileManager-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4000

ENTRYPOINT [ "java", "-jar", "app.jar" ]