# Usa a imagem oficial do Gradle como base para a build
FROM gradle:8-jdk21 AS builder
WORKDIR /app

# Copia os arquivos do projeto
COPY --chown=gradle:gradle . .

# Executa o build do projeto
RUN gradle clean build -x test

# Usa uma imagem menor para rodar a aplicação
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copia o JAR gerado na etapa anterior
COPY --from=builder /app/build/libs/*.jar app.jar

# Expõe a porta usada pela aplicação
EXPOSE 8080

# Define o comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
