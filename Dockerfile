FROM openjdk:alpine
EXPOSE 80
ADD . /
CMD ["java", "-jar", "/build/libs/task-all-0.1.jar"]
