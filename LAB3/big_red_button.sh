#!/bin/sh
CYAN='\033[0;36m'
WHITE='\033[1;37m'

echo " ${CYAN} ------------------------------------ EPOCH 1 - Building image------------------------------------ ${WHITE}"
./docker-build.sh
echo " ${CYAN} ------------------------------------ EPOCH 2 - Launching container------------------------------------ ${WHITE}"
./docker-launch.sh
echo " ${CYAN} ------------------------------------ EPOCH 3 - Attach CLI to container------------------------------------ ${WHITE}"
sudo docker container attach wikidozor-stack
