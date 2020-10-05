FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD target/ta4j-0.0.1-SNAPSHOT.jar ta4j.jar
RUN echo "Asia/Kuala_Lumpur" > /etc/timezone
# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ta4j.jar"]
