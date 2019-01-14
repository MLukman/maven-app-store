FROM maven:3-jdk-8
MAINTAINER anatilmizun@gmail.com
EXPOSE 8090 9090

VOLUME /app
WORKDIR /app

COPY . /src

RUN cd /src && \
    mvn clean install -B && \
    cp $(ls -1t /src/*.jar | head -1) /app/ && \
    cp $(ls -1t /src/*.yml | head -1) /app/ && \
    rm -fr /src

CMD java -jar \
    $(ls -1t *.jar | head -1) server \
    $(ls -1t *.yml | head -1)