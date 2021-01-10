#!/bin/sh
export APP_DIR=/root/SparkWorkdir

spark-submit \
--class 'MetricAggregationApp' \
--master 'spark://localhost:7077' \
--deploy-mode cluster \
--num-executors 1 --executor-memory 2g --executor-cores 4 --driver-memory 2g  --driver-cores 4 \
$APP_DIR/HW2-Spark.jar \
$APP_DIR/MetricAgg.cfg \
$APP_DIR/data2.txt \
$APP_DIR/out 1m
