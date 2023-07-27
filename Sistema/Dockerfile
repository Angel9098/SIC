# Usa una imagen base de Java para tu versión específica (ejemplo: Java 11)
FROM openjdk:11-jre-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR generado de tu proyecto Spring Boot al contenedor
COPY target/Sistema-0.0.1-SNAPSHOT.jar sistema.jar

# Expone el puerto que utilizará tu aplicación (asegúrate de que coincida con la configuración de Spring Boot)
EXPOSE 8080

# Comando para ejecutar tu aplicación cuando se inicie el contenedor
CMD ["java", "-jar", "sistema.jar"]