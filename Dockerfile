
# ==========================================
# ETAPA 1: CONSTRUIR APLICACION
# ==========================================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar archivos Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Copiar codigo fuente
COPY src ./src

# Construir aplicacion Spring Boot
RUN chmod +x mvnw && ./mvnw -B clean package -DskipTests


# ==========================================
# ETAPA 2: EJECUTAR APLICACION
# ==========================================
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar archivo JAR generado
COPY --from=build /app/target/*.jar app.jar

# Puerto de la aplicacion
EXPOSE 10000

# Ejecutar Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
