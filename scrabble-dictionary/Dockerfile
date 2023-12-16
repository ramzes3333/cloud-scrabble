FROM maven:3.9.5-eclipse-temurin-21 AS dependencies
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app
COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /app/pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21.0.1_12-jre
WORKDIR /app
COPY --from=build /app/target/scrabble-dictionary-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]