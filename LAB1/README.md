# Lab 1
Task: gathering count of browser users from httpd logs.  
Tech: Hadoop.  

src - source files.  
fakelog - synthetic data generation script.  
guide.docx - more detailed manual, in russian.  
pom.xml - maven xml.  
Report.zip - lab report, required by course rules.  

# Requirements  
1. RPM-based distribution, Linux kernel 3.10 or higher.
2. JDK 1.8  
3. Hadoop 2.10.0  
4. snappy-1.1.0  
5. maven  
Optional for data generator:  
6. perl5  
7. cpan  
8. Data::Faker from CPAN  

# Build  
1. Download src and pom.xml  
2. Launch   
    ```console  
    foo@bar:~/target$ mvn install  
	```    
for build and test. Resulting jar will be located in "target" directory.  

# Launch  
1. Starting namemode from hadoop home (/opt/hadoop-2.10.0 here)          
   ```console
    foo@bar:/opt/hadoop-2.10.0# sbin/start-dfs.sh
    ```    
2. Put input data into hdfs
   ```console
    foo@bar:/opt/hadoop-2.10.0$ hadoop dfs -put <local path> <hdfs path>
    ```  
3. Launch the application:     
    ```console
    foo@bar:~/HW1-Haloop$ jar ./target/HW1-Haloop-1.0-SNAPSHOT-jar-with-dependencies.jar <input dir in hdfs> <output dir in hdfs>  
    ```  

# Генератор данных   
1. Download fakelog directory.  
2. Install cpan:  
    ```console
    foo@bar:~# yum install cpan
    ```   
3. Install Data::Faker:  
    ```console
    foo@bar:~# cpan install Data::Faker
    ```  
4. cd to fakelog and generate data by executing:  
    ```console
    foo@bar:~/fakelog$ perl ./fakelog.pl <number of lines> <invalid lines percentage> <output file>  
    ```  
 
 
## Lab requirements
|Requirement  |Status   |
|---|---|
|For input information from a)-2 count how many users of IE, Mozilla or other were detected   |  + |
|SequenceFile with Snappy encoding (Read content of compressed file from console using command line)   | +  |
|Combiner is used   | + |
|Report | +  |
  
|Report requirement  |Status   |
|---|---|
|1.	IDE agnostic build: Maven, Ant, Gradle, sbt, etc (10 points)| + |
|2.	Unit tests are provided (20 points) | + |
|3.	Code is well-documented (10 points) | + |
|4.	Script for input file generation or extraction to HDFS must be provided (10 points) | + |
|5.	Working MapReduce application that corresponds business logic, input/output format and additional Requirements that has been started on Hadoop cluster (not singleton mode) (30 points) | + |
