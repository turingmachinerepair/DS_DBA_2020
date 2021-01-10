#!/bin/sh

CYAN='\033[0;36m'
WHITE='\033[1;37m'
WORKDIR=/opt/elk-stack

echo " ${CYAN} ------------------------------------ STAGE 1 - ES ------------------------------------ ${WHITE}"
sudo service  elasticsearch start
echo " ${CYAN} ------------------------------------ STAGE 2 - Kibana ------------------------------------ ${WHITE}"
sudo service  kibana start
echo " ${CYAN} ------------------------------------ STAGE 3 - logstash ------------------------------------ ${WHITE}"
/usr/share/logstash/bin/logstash -f $WORKDIR/logstash.conf 1>$WORKDIR/logstash_1.log 2>$WORKDIR/logstash_2.log &
echo " ${CYAN} ------------------------------------ STAGE 4 - wikiedit logger ------------------------------------ ${WHITE}"

mkdir input
sleep 1m
curl -X POST http://localhost:5601/api/saved_objects/_import?createNewCopies=true -H "kbn-xsrf: true" --form file=@export.ndjson

python3 $WORKDIR/wikistream.py $WORKDIR
