FROM openjdk:8-jdk-alpine
MAINTAINER eko.zhan<eko.z@outlook.com>

ENV MYPATH /app
WORKDIR $MYPATH

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /app/app.jar
COPY application.properties /app/config/

RUN echo "Asia/Shanghai" > /etc/timezone

ENTRYPOINT ["java", "-Xmx512M", "-jar", "/app/app.jar", "--spring.config.location=file:/app/config/application.properties"]