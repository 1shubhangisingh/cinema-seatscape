# Use a Maven image to build the JAR
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy source code
COPY pom.xml .
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# Use a smaller JDK image to run the JAR
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]
