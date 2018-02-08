FROM openjdk:alpine
EXPOSE 80
ADD . /
CMD ["java", "-jar", "/build/libs/jsonvalidator.jar"]
