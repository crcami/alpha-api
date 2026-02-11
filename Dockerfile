# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/target/quarkus-app/ /app/

ENV QUARKUS_HTTP_HOST=0.0.0.0
EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]
