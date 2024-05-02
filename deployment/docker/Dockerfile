# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY /src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
# Define variable
ARG PROFILE=local
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/spring.authentication-*.jar /app/

EXPOSE 8080

ENV DB_URL=jdbc:mysql://mysql:3306/authenticate_db
ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

CMD java -jar -Dspring-profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} spring.authentication-${JAR_VERSION}.jar
