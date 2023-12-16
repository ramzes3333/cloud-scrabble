FROM maven:3.9.6-eclipse-temurin-17 AS dependencies
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /app/pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17.0.9_9-jre
WORKDIR /app
COPY --from=build /app/target/scrabble-game-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]