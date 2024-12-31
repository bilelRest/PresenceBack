FROM openjdk:17-jdk-alpine

RUN apk add --no-cache maven

COPY pom.xml .
COPY src ./src
COPY app-release.aab .

COPY target/presence-0.0.1-SNAPSHOT.jar /app/presence-0.0.1-SNAPSHOT.jar
COPY src/main/resources/application.properties /app/src/main/resources/application.properties

EXPOSE 8085
CMD ["java", "-jar", "/app/presence-0.0.1-SNAPSHOT.jar"]
