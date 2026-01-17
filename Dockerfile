# build the jar using maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# prep the runner image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# grab the jar file from the build stage
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# keeping the app port open
EXPOSE 8080
# start it up
ENTRYPOINT ["java", "-jar", "app.jar"]
