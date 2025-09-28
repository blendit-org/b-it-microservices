# FROM eclipse-temurin:24-jdk

# WORKDIR /app

# COPY target/auth-api-0.0.9-SNAPSHOT.jar app.jar

# EXPOSE 8080

# ENTRYPOINT [ "java", "-jar", "app.jar" ]

# Use JDK 17 runtime
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
