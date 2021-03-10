FROM maven:3-jdk-8-alpine

WORKDIR /usr/db-connector
COPY ./db-connector /usr/db-connector
RUN mvn clean install

WORKDIR /usr/server
COPY ./server /usr/server
RUN mvn clean package

EXPOSE 3000

CMD ["java", "-jar", "./target/sparta-server-1.0-SNAPSHOT-jar-with-dependencies.jar"]
