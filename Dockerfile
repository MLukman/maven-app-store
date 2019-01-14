FROM maven:3-jdk-8
MAINTAINER anatilmizun@gmail.com
EXPOSE 8090 9090

WORKDIR /app

COPY . /src

RUN cd /src && \
    export MAVEN_OPTS="-Dmaven.repo.local=/src/maven.repository" && \
    mvn clean install -B && \
    mkdir /appstore
    cp $(ls -1t /src/target/*.jar | head -1) /appstore/ && \
    cp $(ls -1t /src/*.yml | head -1) /appstore/ && \
    cp /src/docker-run.sh /appstore/ && \
    chmod +x /appstore/docker-run.sh
    rm -fr /src

VOLUME /app

CMD /appstore/docker-run.sh