# syntax=docker/dockerfile:1.7

##
## Etapa 1: build del JAR con Maven + Java 21
##
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiamos primero archivos de build para aprovechar cache de dependencias
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Descarga de dependencias (cacheable)
RUN ./mvnw -q -DskipTests dependency:go-offline

# Código fuente y compilación
COPY src ./src
RUN ./mvnw -q clean package -DskipTests


##
## Etapa 2: runtime liviano (solo JRE)
##
FROM eclipse-temurin:21-jre

WORKDIR /app

# Usuario sin privilegios para ejecutar la app
RUN useradd -m -u 1001 springuser
RUN mkdir -p /app/uploads && chown -R springuser:springuser /app/uploads

# Copiamos el jar generado (Spring Boot repackaged jar)
COPY --from=builder /app/target/*.jar /app/app.jar

ENV JAVA_OPTS=""
ENV UPLOAD_DIR="/app/uploads"

# Render inyecta PORT; si no existe, usa 8080 localmente
EXPOSE 8080

USER springuser

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
