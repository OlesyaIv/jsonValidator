FROM openjdk:8-jdk-alpine
CMD ["java","-jar","target/jsonvalidator-0.1-jar-with-dependencies.jar"]
EXPOSE 80
