FROM maven:3.6.3-jdk-11 as buildContainer
RUN mkdir /app
WORKDIR /app
ADD src src
copy pom.xml .
copy zip-assembly.xml .
RUN mvn clean package
FROM alpine as executionContainer
RUN mkdir -p /app/workspace
RUN mkdir -p /app/target
RUN apk update && apk upgrade
RUN apk add openjdk11
WORKDIR /app
COPY --from=buildContainer /app/target/coverage-parser.jar .
COPY execution.sh .
RUN chmod 100 execution.sh
CMD ["/bin/ash", "-c", "java -jar coverage-parser.jar;"]