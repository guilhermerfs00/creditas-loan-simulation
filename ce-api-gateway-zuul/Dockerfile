FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
WORKDIR /app

COPY --from=builder /app/target/ce-api-gateway-zuul-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8765

ENTRYPOINT ["java","-jar","app.jar"]
