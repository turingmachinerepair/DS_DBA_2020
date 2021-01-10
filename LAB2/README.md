# Lab 2  
Task: aggregating abstract metrics by timecode into time intervals.  
Tech: Apache Spark + Apache Ignite + Apache Kafka.  
 
src - source files.  
FakeMetricGen - synthetic data generation script.  
utility - additional scripts for demonstration convenience.  
pom.xml - maven xml.  
MetricAgg.cfg - application configuration. 

# Requirements  
1. RPM-based distribution, Linux kernel 3.10 or higher. 
2. JDK 1.8  
3. Spark 2.4.7 
4. Ignite 2.9.0
5. Kafka 2.4.1
6. maven  
7. Scala 2.11

# Build  
1. Download src and pom.xml  
2. Execute in build directory:  
    ```console
    mvn install  
    ```
for build and testing. Resulting jar will be placed in "target" directory.  

# Launch  

1. Launch Kafka  service
2. Launch master and slave Spark nodes  
3. Launch application  
    ```console
    foo@bar:spark-submit --class 'MetricAggregationApp' --master '<spark master URL>'\
    --deploy-mode cluster \
    <jar-file> <system configuration file> <input file> <output directory> <time interval>
    ```

# Генератор данных 
For testing convenience synthetic input data generator is provided.  

1. Download FakeMetricGen.  
2. In FakeMetricGen directory launch:  
    ```console
    foo@bar:~/fakelog$ perl ./fakeMetric.pl <number of lines> <invalid lines percentage> <output directory>  
    ```
 
## Текущий статус
|Lab requirement  |Status   |
|---|---|
|2.	Program which aggregate raw metrics into selected scale.  Data input format: metricId, timestamp, value  Data output format: metricId, timestamp, scale, value  |  + |
|1. Kafka producer   | +  |
|3. Ignite Native Persistence   | + |
|1. Spark RDD | +  |
  
|Report requirement  |Status   |
|---|---|
|1.	IDE agnostic build: Maven, Ant, Gradle, sbt, etc (10 points)| + |
|2.	Unit tests are provided (20 points) | - |
|3.	Code is well-documented (10 points) | + |
|4.	Script for input file generation or calculation setup and control (10 points) | + |
|5.	Working application that corresponds business logic, input/output format and additional Requirements that has been started on cluster (30 points) | + |
|6. The relevant report was prepared (20 points) | + |
