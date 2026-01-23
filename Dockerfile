FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# rename jar to app.jar
RUN cp target/*.jar app.jar

EXPOSE 8084

CMD ["java", "-jar", "target/*.jar"]
