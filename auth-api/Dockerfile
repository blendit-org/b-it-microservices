FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY target/auth-api-0.0.5-SNAPSHOT.jar app.jar

EXPOSE 8000

ENTRYPOINT [ "java", "-jar", "app.jar" ]