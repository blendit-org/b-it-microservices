FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY target/projectStatusUpdate-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4001

ENTRYPOINT [ "java", "-jar", "app.jar" ]