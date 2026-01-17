# first we build the app with maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# grab the pom and source code
COPY pom.xml .
COPY src ./src
# build the jar and skip tests to save time
RUN mvn clean package -DskipTests

# now we set up the actual runner
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# copy the jar we just made
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# keep an eye on the app health
HEALTHCHECK --interval=30s --timeout=3s \
    CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

# open up the port 
EXPOSE 8080
# let's get this thing running
ENTRYPOINT ["java", "-jar", "app.jar"]
