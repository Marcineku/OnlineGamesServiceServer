FROM openjdk:8-jre-alpine
ADD target/server-1.0.0.jar server-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "server-1.0.0.jar"]
