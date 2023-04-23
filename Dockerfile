FROM openjdk:17

COPY build/libs/bookMyTable-1.0.jar /bookMyTable.jar
EXPOSE 8080

CMD java -jar bookMyTable.jar