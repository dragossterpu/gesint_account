FROM java:8-jre
MAINTAINER Dragos Adrian Sterpu <dragossterpu@gmail.com>

ADD ./target/gateway.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/gateway.jar"]

EXPOSE 4000