FROM openjdk:17-jdk-slim

WORKDIR /app

COPY /target/wms-backend-1.0-SNAPSHOT.jar /app/wms-backend.jar

EXPOSE 8080