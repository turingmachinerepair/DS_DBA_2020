package MetricAggregation;

import MetricAggregation.kafka.KafkaIgniteStreamer;
import org.apache.ignite.Ignite;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.SparkConf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import scala.Tuple2;

import org.apache.ignite.spark.JavaIgniteContext;
import org.apache.ignite.spark.JavaIgniteRDD;

import org.apache.ignite.Ignition;
import MetricAggregation.metrickey.MetricKey;
import MetricAggregation.bucketsignature.BucketSignature;
import MetricAggregation.kafka.KafkaWriter;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.FileHandler;

/**
 * Application business logic class.
 * Initializes Kafka and Ignite instances.
 * Initializes Kafka,Spark and Ignite contexts.
 * Loads metric records from input file.
 * Performs grouping of metric measurements into buckets with specified time interval. Measurement value is averaged for single bucket.
 * @author Petrov A.N.
 */
public class MetricAggregation {
    //general parameters
    /** Input file
     */
    private static String iFile;
    /** Output file
     */
    private static String oFile;
    /** Input timescale represented by string.
     */
    private static String scaleString;

    //spark parameters
    /** App name.
     */
    private static String appName;
    /** Hostname of spark master.
     */
    private static String sparkHostname;

    //Kafka parameters
    /** Kafka topic for file parsing and loading data to Ignite.
     */
    private static String kafkaInputTopic;
    /** Kafka instance hostname.
     */
    private static String kafkaHostname;

    //Ignite parameters

    /** Ignite instance configuration filepath.
     */
    private static String confIgnite;
    /** Cache name for storing input records.
     */
    private static String cacheName;
    /** Cache name for storing processed data (grouped measurements).
     */
    private static String outputCacheName;


    /** Spark context.
     */
    private static JavaSparkContext sc;
    /** Kafka producer instance, parses input file to Kafka topic.
     */
    private static KafkaWriter kReader;
    /** Kafka consumer instance, reads data from topic and puts records into Ignite input cache.
     */
    private KafkaIgniteStreamer kStream;
    /** Ignite instance
     */
    private static Ignite  igniteInstance;

    Logger mainLog;

    /**
     * Argument processing method.
     * 1st parameter - path to system configuration.
     * 2nd parameter - input file
     * 3d parameter - output directory
     * 4th parameter - scale
     * System configuration consists of following parameters:
     *
     * spark.hostname - IP of Spark master
     * spark.appname - application name
     * kafka.hostname - IP of Kafka service
     * kafka.topic - topic for input
     * ignite.config - path to ignite xml configuration
     * ignite.input-cache - name of cache for input
     * ignite.output-cache - name of cache for output
     *
     * @param args arguments passed to application
     */
    public void processParameters(String[] args){
        if(args.length < 4)
            return;

        String confPath = args[0];
        iFile = args[1];
        oFile = args[2];
        scaleString = args[3];

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    confPath));
            String line = "-";
            while (line != null) {
                //System.out.println(line);
                // read next line
                line = reader.readLine();
                if( line != null) {
                    String[] lineParts = line.split("=");
                    if(lineParts.length == 2){
                        String parameter = lineParts[0];
                        String value = lineParts[1];
                        switch(parameter){
                            case("spark.hostname"): sparkHostname = value; break;
                            case("spark.appname"): appName = value; break;

                            case("kafka.hostname"): kafkaHostname = value; break;
                            case("kafka.topic"): kafkaInputTopic = value; break;

                            case("ignite.config"): confIgnite = value; break;
                            case("ignite.input-cache"): cacheName = value; break;
                            case("ignite.output-cache"): outputCacheName = value; break;
                        }

                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialization method. Sets instance parameters according to parsed configuration.
     * Instantiates Spark context, Kafka consumer and producer, launches Ignite instance.
     */
    public void init(){
        //cacheName = "HW2Input";
        //outputCacheName = "HW2Output";
        //confIgnite = "hw2-ignite-config.xml";
        //appName = "MetricAggregation";

        //Spark setup
        SparkConf conf = new SparkConf().setAppName(appName).setMaster(sparkHostname);
        sc = new JavaSparkContext(conf);

        //Ignite
        igniteInstance = Ignition.start(confIgnite);
        igniteInstance.active(true);

        //Kafka producer
        kReader = new KafkaWriter();
        kReader.setTopicName(kafkaInputTopic);
        kReader.setKafkaHostname(kafkaHostname);
        kReader.setup();

        //Kafka consumer
        kStream = new KafkaIgniteStreamer( kafkaHostname, cacheName, kafkaInputTopic, igniteInstance);
        kStream.initialize();

    }

    /**
     * Converter method, parses scale string into milliseconds since UNIX epoch.
     * String must have format %d%s, where %d - positive integer, %s - time unit.
     * Accepted time units: ms(milliseconds), s(seconds), m(minutes), h(hours), d(days), w(weeks)
     * @param str - scale represented by string.
     * @return milliseconds since UNIX epoch
     */
    public Long scaleToMillisSinceEpoch(String str){
        Long res= 0L;

        String arr1[]=str.split("[^0-9]+");
        String arr2[]=str.split("[0-9]+");

        if( arr1.length != 1 || arr2.length != 2 ){
            res = -1L;
            return res;
        }

        //System.out.println( arr1[0]+" "+arr2[1] );

        Long val = Long.parseLong(arr1[0]);

        switch(arr2[1]){
            case "ms": res = val;   break;
            case "s":  res = val*1000; break;
            case "m":  res = val*1000*60; break;
            case "h":  res = val*1000*60*60; break;
            case "d":  res = val*1000*60*60*24; break;
            case "w":  res = val*1000*60*60*24*7; break;
            //case "mon": res = val*1000*60*60*24*7*4; break;
            //case "y": res = val*1000*60*60*24*7*4*12;
        }

        //System.out.println(res);


        return res;
    }

    /**
     * Utility method, starts Kafka-&gt;Ignite stream.
     */
    private void startIgniteStreaming(){
        KafkaIgniteStreamer.streamStart();
    }

    /**
     * Utility method, stops Kafka-&gt;Ignite stream.
     */
    private void endIgniteStreaming(){

        kStream.streamStop();
    }

    /**
     * Utility method, halting execution for 5 seconds.
     */
    private void localWait(){
        try{
            Thread.sleep(5000);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Main method, performs task specified by HW variant.
     * Reads file with records in format of &lt;metricID, timestamp (millis since UNIX epoch), value&gt;to Kafka topic.
     * Reads from Kafka topic to Ignite cache.
     * Reassigns input records to output records of following format: &lt;metricID, timestamp (millis since UNIX epoch, rounded to lower boundary according to scale), scale, value&gt;
     * If multiple input records map to one output record, value in output is calculated as average of input values.
     * Saves result to Ignite cache.
     * Dumps output Ignite cache.
     */
    public void exec(){
        Long scale = scaleToMillisSinceEpoch(scaleString);
        String sscale = scaleString;
        System.out.println("Parsed scale:"+scale.toString());
        if(scale < 0 ){
            return;
        }

        mainLog = Logger.getLogger("mainLogger");
        FileHandler fh;
        try{
            fh = new FileHandler("mainDbg.log");
            mainLog.addHandler(fh);

            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (Exception e){
            e.printStackTrace();
        }

        mainLog.info("STAGE 0.1 - initialize instances and contexts");
        init();
        mainLog.info("STAGE 0.1 - Completed. Giving extra time for services to start/prepare.");
        localWait();

        //STAGE 0.2 - open Kafka->Ignite stream
        mainLog.info("STAGE 0.2 - Open Kafka->Ignite stream.");
        startIgniteStreaming();
        mainLog.info("STAGE 0.2 - Completed. Giving extra time for services to start/prepare.");
        localWait();

        //STAGE 0.3 - process input file to Kafka
        mainLog.info("STAGE 0.3 - Open Kafka->Ignite stream.");
        kReader.processFileToKafka(iFile); //load data to kafka
        mainLog.info("STAGE 0.3 - Completed. Giving extra time for services to start/prepare.");
        localWait();

        //STAGE 0.4 - close Kafka->Ignite stream
        mainLog.info("STAGE 0.4 - Close Kafka->Ignite stream.");
        endIgniteStreaming();
        mainLog.info("STAGE 0.4 - Completed. Giving extra time for services to start/prepare.");

        mainLog.info("STAGE 1 - Read data from Ignite cache, containing records parsed from input file.");
        JavaIgniteContext<MetricKey, Integer> igniteContext = new JavaIgniteContext<MetricKey, Integer>(sc,confIgnite);
        JavaIgniteRDD<MetricKey, Integer> sharedRDD = igniteContext.<Integer, Integer>fromCache(cacheName);
        Long ct = sharedRDD.count();
        mainLog.info("STAGE 1 - Completed. RDD contains:"+ct.toString()+" records");

        mainLog.info("STAGE 2 - Map input records to output format. Reduce timestamp to lower boundary of corresponding bucket and append scale string");
        JavaPairRDD<BucketSignature,Integer> rescaled = sharedRDD.mapToPair(
                s -> {
                    MetricKey mKey = s._1;
                    Long metricID = mKey.getMetricID();
                    Date tstamp = mKey.getTimestamp();
                    Integer value = s._2;
                    Date newDate = new Date ( tstamp.getTime() - (tstamp.getTime())%scale);
                    BucketSignature newKey = new BucketSignature(metricID, newDate, sscale );
                    Tuple2< BucketSignature, Integer > keyValuePair = new Tuple2< BucketSignature, Integer >(newKey,value);
                    return keyValuePair;
                }
        );
        mainLog.info("STAGE 2 - Completed.");


        mainLog.info("STAGE 3 - Group by time bucket");
        JavaPairRDD< BucketSignature, Long > grouped = rescaled.groupByKey().mapToPair(
                s->{
                    Long avg = 0L;
                    Long count = 0L;
                    for(Integer it: s._2){
                        count++;
                        avg += it.longValue();
                    }
                    return new Tuple2< BucketSignature, Long > ( s._1, avg/count);
                }
        );
        mainLog.info("STAGE 3 - Completed. RDD now contains ");

        //STAGE 4 - write to ignite
        mainLog.info("STAGE 4 - Write to Ignite cache.");
        JavaIgniteContext<BucketSignature, Long> igniteContext2 = new JavaIgniteContext<BucketSignature, Long>(sc,confIgnite);
        JavaIgniteRDD<BucketSignature, Long> savingRDD = igniteContext2.fromCache(outputCacheName);
        savingRDD.savePairs(grouped);
        mainLog.info("STAGE 4 - Completed.");

        JavaIgniteRDD<BucketSignature, Long> checkingRDD = igniteContext2.fromCache(outputCacheName);
        checkingRDD.coalesce(1).saveAsTextFile(oFile);
        mainLog.info("For testing purposes output cache is saved to:"+oFile);

        sc.close();

    }


}
