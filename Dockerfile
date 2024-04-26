FROM openjdk:17-jdk-slim
COPY target/spring.authentication-1.0.0.jar authentication-app.jar
ENTRYPOINT ["java", "-jar", "authentication-app.jar"]