FROM maven:3-jdk-8
MAINTAINER anatilmizun@gmail.com
EXPOSE 8090 9090

VOLUME /app
WORKDIR /app

COPY . /src

RUN cd /src && \
    export MAVEN_OPTS="-Dmaven.repo.local=/src/maven.repository" && \
    mvn clean install -B && \
    cp $(ls -1t /src/target/*.jar | head -1) /app/ && \
    if [ ! -f "/app/configuration.yml" ]; then cp /src/configuration.yml /app/ ; fi && \
    rm -fr /src

CMD java -jar \
    $(ls -1t *.jar | head -1) server \
    configuration.yml