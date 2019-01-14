#!/bin/sh

cp /appstore/*.jar /app/
if [ ! -f "/app/configuration.yml" ]; then cp /appstore/configuration.yml /app/ ; fi

cd /app
java -jar $(ls -1t *.jar | head -1) server configuration.yml
 