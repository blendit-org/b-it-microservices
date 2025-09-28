# FROM eclipse-temurin:24-jdk

# WORKDIR /app

# COPY target/projectFileManager-0.0.1-SNAPSHOT.jar app.jar

# EXPOSE 4000

# ENTRYPOINT [ "java", "-jar", "app.jar" ]

FROM eclipse-temurin:24-jdk AS build
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]