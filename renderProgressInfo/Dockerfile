FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY target/renderProgressInfo-0.0.2-SNAPSHOT.jar app.jar

EXPOSE 8010

ENTRYPOINT [ "java", "-jar", "app.jar" ]