 
FROM ubuntu:latest
WORKDIR /opt/elk-stack
RUN apt-get update && apt-get install -y sudo gnupg2 && rm -rf /var/lib/apt/lists/*
ADD setup.sh .
ADD startup.sh .
ADD wikistream.py .
ADD logstash.conf .
ADD export.ndjson .
RUN chmod +x ./setup.sh 
RUN chmod +x ./startup.sh
RUN ./setup.sh
EXPOSE 8080
EXPOSE 5601

