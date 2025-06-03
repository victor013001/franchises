# Build stage
FROM gradle:8.3-jdk17 AS build

COPY . /app
WORKDIR /app

RUN gradle clean build -x test

# Production stage
FROM openjdk:17-jdk-alpine AS production

EXPOSE 8080

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT [ "java", "-jar", "/app.jar" ]
