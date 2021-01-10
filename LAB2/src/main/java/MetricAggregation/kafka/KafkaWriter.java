package MetricAggregation.kafka;


import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileReader;
import java.io.IOException;import java.util.Properties;

import org.apache.kafka.clients.producer.RecordMetadata;
import scala.Tuple2;
import java.io.*;

import MetricAggregation.metrickey.MetricKey;

/**
 * Worker class for parsing file to Kafka.
 * @version 1.0
 * @author PhDInIntegrals
 */
public class KafkaWriter {
    private String hostname;
    /** Topic name to put file lines to.
     */
    private String topicName;

    /** Producer instance.
     */
    private Producer<MetricKey, Integer> producer;

    /**
     * Target topic setter.
     * @param _topicName target topic name
     */
    public void setTopicName(String _topicName){
        topicName = _topicName;
    }

    public void setKafkaHostname(String _hostname){
        hostname = _hostname;
    }

    /**
     * Kafka connection setup.
     */
    public void setup(){
        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", hostname);
        //Set acknowledgements for producer requests.
        props.put("acks", "1");
        //If the request fails, the producer can automatically retry,
        props.put("retries", 1);
        //Specify buffer size in config
        props.put("batch.size", 16384);
        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);
        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);
        props.put("client.id","m_agg");
        props.put("auto.commit.reset","beginning");
        props.put("auto.offset.reset","earliest");

        props.put("key.serializer","MetricAggregation.metrickey.MetricKeySerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.IntegerSerializer");

        producer = new KafkaProducer(props);
        producer.flush();
        System.out.println( "Started producer" );
        System.out.println( producer.toString() );
    }

    /**
     * Parse file to kafka topic.
     * @param fname Filename to parse to kafka.
     */
    public void processFileToKafka(String fname) {
        producer.flush();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    fname));
            String line = "-";
            while (line != null) {
                //System.out.println(line);
                // read next line
                line = reader.readLine();
                if( line != null){
                    String[] lineParts = line.split(",");
                    if( lineParts.length == 3 ){
                        try{
                            Long metricID = Long.parseLong( lineParts[0].trim() );
                            Long timestamp = Long.parseLong( lineParts[1].trim() );
                            Integer metric = Integer.parseInt( lineParts[2].trim() );
                            Tuple2<Long,Long> keyTuple = new Tuple2<Long,Long>(metricID,timestamp);
                            MetricKey key = new MetricKey( keyTuple );
                            ProducerRecord<MetricKey, Integer> pRec = new ProducerRecord<MetricKey, Integer>(topicName,key,metric);
                            RecordMetadata mtd = producer.send( pRec ).get();
                            //System.out.println(mtd.toString());
                        }  catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    /**
     * Close Kafka producer.
     */
    public void close(){
        producer.close();
    }
}
