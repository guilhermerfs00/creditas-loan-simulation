FROM eclipse-temurin:21-jdk
WORKDIR /app

# Instalar Maven e construir o projeto
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
COPY . .
RUN mvn clean package

# Copiar o artefato gerado diretamente após o build
COPY target/ce-eureka-server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
