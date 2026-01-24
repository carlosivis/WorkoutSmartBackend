FROM gradle:8-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle buildFatJar --no-daemon

FROM eclipse-temurin:21-jdk
EXPOSE 8080
RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*-all.jar /app/ktor-server.jar

COPY src/main/resources/firebase-admin.json /app/resources/firebase-admin.json

WORKDIR /app
CMD ["java", "-jar", "ktor-server.jar"]