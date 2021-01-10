#!/bin/sh
CYAN='\033[0;36m'
WHITE='\033[1;37m'
DEBIAN_FRONTEND="noninteractive"
WORKDIR=/opt/elk-stack

#STAGE 1 - Java & co
sudo apt-get update

#software for installation and repo management
echo " ${CYAN} ------------------------------------ STAGE 1.1 - REPO, CERTS------------------------------------ ${WHITE}"
sudo apt-get install -y --no-install-recommends apt-transport-https ca-certificates curl software-properties-common wget apt-utils dialog
    
echo " ${CYAN} ------------------------------------STAGE 1.2 - JAVA------------------------------------ ${WHITE}"
sudo add-apt-repository ppa:openjdk-r/ppa #openjdk repo
sudo apt-get install -y openjdk-8-jdk 

#-----------------------------------------------------------------------------------------
#STAGE 2 - ES
echo " ${CYAN} ------------------------------------ STAGE 2 - elasticsearch ------------------------------------ ${WHITE}"
echo " ${CYAN} ------------------------------------ STAGE 2.1 - Preparations------------------------------------ ${WHITE}"
wget -qO - https://artifacts.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add -
sudo apt-get install apt-transport-https
echo deb https://artifacts.elastic.co/packages/7.x/apt stable main | sudo tee -a /etc/apt/sources.list.d/elastic-7.x.list

echo " ${CYAN} ------------------------------------ STAGE 2.2 - Install ES------------------------------------ ${WHITE}"
sudo apt-get update
sudo apt-get install -y elasticsearch

#STAGE 2.3 - Configure ES
echo " ${CYAN} ------------------------------------ STAGE 2.3 - Configure ES------------------------------------ ${WHITE}"
echo network.host: localhost >> /etc/elasticsearch/elasticsearch.yml
echo http.port: 9200 >> /etc/elasticsearch/elasticsearch.yml
echo discovery.type: single-node >> /etc/elasticsearch/elasticsearch.yml

#STAGE 3 - Kibana
echo " ${CYAN} ------------------------------------ STAGE 3 - Kibana ------------------------------------ ${WHITE}"
#STAGE 3.1 - Install Kibana
sudo apt-get install -y kibana

#STAGE 3.2 - Configure Kibana
echo server.port: 5601 >> /etc/kibana/kibana.yml
echo server.host: 0.0.0.0 >> /etc/kibana/kibana.yml
echo elasticsearch.hosts: http://localhost:9200 >> /etc/kibana/kibana.yml

#-----------------------------------------------------------------------------------------
#STAGE 4 - Logstash
echo " ${CYAN} ------------------------------------ STAGE 4 - Logstash ------------------------------------ ${WHITE}"
#STAGE 4.1 - Install Logstash
sudo apt-get install -y logstash

#STAGE 4.2 - Configure Logstash

#STAGE 4.3 - Enable Logstash
#logstash -f ./logstash.conf &

#-----------------------------------------------------------------------------------------
#STAGE 5 - My application time
#mkdir input
#python3.6 wikistream.py &

sudo apt-get install -y python3-pip
sudo pip3 install sseclient wikipedia-api elasticsearch


