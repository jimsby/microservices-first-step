FROM maven:3.8.5-openjdk-17-slim as builder

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package -Dmaven.test.skip=true

FROM openjdk:17-alpine
COPY --from=builder target/resource-p-0.0.1-SNAPSHOT.jar application.jar

EXPOSE 8080

CMD java -jar application.jar