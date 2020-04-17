FROM java:8-jre
MAINTAINER Dragos Adrian Sterpu <dragossterpu@gmail.com>

ADD ./target/notification-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/notification-service.jar"]

EXPOSE 8000