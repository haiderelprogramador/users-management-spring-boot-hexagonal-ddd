# syntax=docker/dockerfile:1
# =============================================
# Etapa 1: compilacion con Maven (no requiere Maven/JDK en la maquina host)
# =============================================
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
# Sin -q para ver el detalle si algo falla durante la compilacion
RUN mvn -B clean package -DskipTests

# =============================================
# Etapa 2: imagen de ejecucion liviana (solo JRE)
# =============================================
FROM eclipse-temurin:17-jre
WORKDIR /app

# Usuario sin privilegios
RUN useradd --system --create-home spring
USER spring

COPY --from=build /app/target/users-management-*.jar app.jar

# Puerto por defecto; en Render se usa la variable PORT que inyecta la plataforma
ENV PORT=8080
EXPOSE 8080

# MaxRAMPercentage: aprovecha la memoria del contenedor (Render free = 512 MB)
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
