FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/*.jar app.jar
EXPOSE 8080

ENV DB_HOST=sql11.freemysqlhosting.net
ENV DB_PORT=3306
ENV DB_NAME=sql11703798
ENV DB_USER=sql11703798
ENV DB_PASS= #password

ENV AWS_ACCESS_KEY_ID= #access key
ENV AWS_SECRET_ACCESS_KEY= #secret key
ENV AWS_REGION=eu-north-1
ENV AWS_BUCKET_NAME=gts-trip-img

ENTRYPOINT ["java", "-jar", "app.jar"]