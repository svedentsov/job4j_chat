FROM openjdk
WORKDIR chat
ADD target/chat-1.0.jar app.jar
ENTRYPOINT java -jar app.jar