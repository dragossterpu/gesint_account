FROM java:8-jre
MAINTAINER Dragos Adrian Sterpu <dragossterpu@gmail.com>

ADD ./target/monitoring.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/monitoring.jar"]

EXPOSE 8080