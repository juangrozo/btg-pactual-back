# Usar la imagen base de OpenJDK 17
FROM openjdk:17-jdk-alpine

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR generado por Spring Boot al contenedor
COPY target/pactual-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que tu aplicación está corriendo
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
