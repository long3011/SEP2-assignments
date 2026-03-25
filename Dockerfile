# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn -B clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

COPY --from=build /app/target/SEP2-assignments-1.0-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

