# 1️⃣ Usamos la imagen oficial de Gradle con Java 17 para compilar
FROM gradle:8.5-jdk17 AS build

# 2️⃣ Establecemos el directorio de trabajo
WORKDIR /app

# 3️⃣ Copiamos los archivos de configuración de Gradle
COPY build.gradle settings.gradle ./

# 4️⃣ Copiamos el código fuente
COPY src ./src

# 5️⃣ Construimos el proyecto (esto genera el JAR)
RUN gradle build -x test --no-daemon

# 6️⃣ Usamos una imagen más liviana de OpenJDK 17 para ejecutar el JAR
FROM openjdk:17-jdk-slim

WORKDIR /app

# 7️⃣ Copiamos el archivo JAR desde la etapa de compilación
COPY --from=build /app/build/libs/*.jar app.jar

# 8️⃣ Exponemos el puerto 8080
EXPOSE 8080

# 9️⃣ Ejecutamos la aplicación
CMD ["java", "-jar", "app.jar", "--server.port=8080"]
