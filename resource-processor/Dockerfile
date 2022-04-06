FROM maven:3.8.4-openjdk-17-slim as builder

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package

FROM openjdk:17-slim
COPY --from=builder target/resource-p-0.0.1-SNAPSHOT.jar application.jar

EXPOSE 8080

CMD java -jar application.jar