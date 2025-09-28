# FROM eclipse-temurin:24-jdk

# WORKDIR /app

# COPY target/scoringSystem-0.0.1-SNAPSHOT.jar app.jar

# EXPOSE 8006

# ENTRYPOINT [ "java", "-jar", "app.jar" ]

FROM eclipse-temurin:24-jdk AS build
WORKDIR /app

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make wrapper executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source
COPY src src

# Build app
RUN ./mvnw package -DskipTests

# Runtime stage
FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]