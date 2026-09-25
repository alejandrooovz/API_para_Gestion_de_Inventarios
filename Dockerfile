# -----------------------------
# Etapa 1: compilación
# -----------------------------
# Se utiliza una imagen con JDK 21 para compilar la aplicación.
FROM eclipse-temurin:21-jdk AS build

# Directorio de trabajo dentro del contenedor.
WORKDIR /app

# Se copian todos los archivos del proyecto al contenedor.
COPY . .

# Se compila el proyecto con Maven Wrapper.
# Se omiten las pruebas en esta etapa para generar el archivo JAR.
RUN ./mvnw clean package -DskipTests

# -----------------------------
# Etapa 2: ejecución
# -----------------------------
# Se utiliza una imagen más ligera con JRE 21
# porque ya no es necesario compilar la aplicación.
FROM eclipse-temurin:21-jre

# Directorio de trabajo de la aplicación.
WORKDIR /app

# Se copia únicamente el archivo JAR generado
# en la etapa de compilación.
COPY --from=build /app/target/*.jar app.jar

# Puerto utilizado por Spring Boot.
EXPOSE 8080

# Comando que se ejecuta al iniciar el contenedor.
ENTRYPOINT ["java", "-jar", "app.jar"]