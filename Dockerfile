FROM openjdk:alpine	
	
EXPOSE 80
RUN apk add --no-cache maven	
ADD src /jsonValidator/src
ADD pom.xml /jsonValidator/pom.xml		
WORKDIR /jsonValidator		
RUN mvn clean install -e		
CMD ["java","-jar","target/jsonvalidator-0.1-jar-with-dependencies.jar"]		
#  sudo docker build -t jsonvalidator .		
#  sudo docker run -d --rm -p 80:80  jsonvalidator
