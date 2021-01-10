#!/bin/sh
/opt/spark-2.4.7-bin-hadoop2.7/sbin/start-master.sh
/opt/spark-2.4.7-bin-hadoop2.7/sbin/start-slave.sh spark://localhost:7077

