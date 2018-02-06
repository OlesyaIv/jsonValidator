FROM openjdk:alpine	 FROM openjdk:alpine
	
EXPOSE 80
RUN apk add --no-cache maven	+CMD ["java", "-jar", "/build/libs/task-all-0.1.jar"]
ADD src /jsonValidator/src	
ADD pom.xml /jsonValidator/pom.xml	
WORKDIR /jsonValidator	
RUN mvn clean install -e	
CMD ["java","-jar","target/task-0.1-jar-with-dependencies.jar"]	
-#  sudo docker build -t task .	
-#  sudo docker run -d --rm -p 80:80  task
