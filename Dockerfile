FROM openjdk:alpine
WORKDIR /jsonValidator
ADD src /jsonValidator/src
CMD ["java","-jar","target/jsonvalidator-0.1-jar-with-dependencies.jar"]
EXPOSE 80
