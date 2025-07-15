# Usar imagem base do Java 21 com JDK
FROM eclipse-temurin:21-jdk

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o .jar gerado pelo Maven (mude o nome se necessário)
COPY target/ce-auth-ms-0.0.1-SNAPSHOT.jar app.jar

# Porta exposta
EXPOSE 8081

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
