package MetricAggregation.kafka;

import MetricAggregation.metrickey.MetricKey;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.lang.IgniteBiTuple;
import org.apache.ignite.Ignite;
import java.util.Arrays;

import java.util.Properties;
/**
 * Class for streaming data from Kafka topic to Ignite cache.
 * @author  Petrov A.N.
 * @version  1.0
 */
public class KafkaIgniteStreamer {

    /** Kafka instance hostname
     */
    private static String kafkaHostname;
    /** Kafka topic to read data from
     */
    private static String targetKafkaTopicName;

    /** Target Ignite cache name
     */
    private static String targetIgniteCacheName;
    /** Target Ignite instance
     */
    private static Ignite igniteInstance;

    /** Kafka streamer instance
     */
    private static org.apache.ignite.stream.kafka.KafkaStreamer<MetricKey, Integer> kafkaStreamer;
    /** Ignite streamer instance
     */
    private static IgniteDataStreamer<MetricKey, Integer> igniteStream;

    /**
     * Constructs instance of Kafka-&gt;Ignite stream.
     * @param _kafkaHostname Kafka hostname
     * @param _targetIgniteCacheName Ignite cache name
     * @param _targetKafkaTopicName Kafka topic name
     * @param _igniteInstance Ignite instance
     */
    public KafkaIgniteStreamer( String _kafkaHostname, String _targetIgniteCacheName,  String _targetKafkaTopicName, Ignite _igniteInstance ){
        kafkaHostname = _kafkaHostname;
        targetKafkaTopicName = _targetKafkaTopicName;
        targetIgniteCacheName = _targetIgniteCacheName;
        igniteInstance=_igniteInstance;
    }

    /**
     * Returns Kafka consumer configuration.
     * @return Kafka consumer configuration.
     */
    private Properties configureKafkaReader(){
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaHostname);
        props.put("group.id", "use_a_separate_group_id_for_each_stream");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("auto.offset.reset","earliest");
        props.put("key.deserializer","MetricAggregation.metrickey.MetricKeyDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.IntegerDeserializer");
        return props;
    }

    /**
     * Construct and initialize stream object instances.
     */
    public void initialize(){
        System.out.println("Kafka->Ignite stream setup");
        System.out.println("Stream from topic "+targetKafkaTopicName+" to cache "+targetIgniteCacheName);

        kafkaStreamer = new org.apache.ignite.stream.kafka.KafkaStreamer<>();
        igniteStream = igniteInstance.dataStreamer(targetIgniteCacheName);

        // allow overwriting cache data
        igniteStream.allowOverwrite(true);

        kafkaStreamer.setIgnite(igniteInstance);
        kafkaStreamer.setStreamer( igniteStream );

        String[] topics = { targetKafkaTopicName };
        // set the topic
        kafkaStreamer.setTopic( Arrays.asList(topics.clone()) );

        // set the number of threads to process Kafka streams
        kafkaStreamer.setThreads(2);

        // set Kafka consumer configurations
        kafkaStreamer.setConsumerConfig( configureKafkaReader() );

        // set extractor
        kafkaStreamer.setSingleTupleExtractor(
                msg -> {
                    MetricKey locKey = (MetricKey) msg.key();
                    Integer locValue = (Integer) msg.value();
                    //System.out.println("RX:" + locKey.toString() + ":" + locValue.toString());
                            /*return new AbstractMap.SimpleEntry<MetricKey, Integer>(
                                    locKey, locValue
                            );*/
                    return new IgniteBiTuple<>( locKey, locValue);
                }
        );


    }

    /**
     * Method starting data streaming.
     */
    public static void streamStart(){
        kafkaStreamer.start();
        System.out.println("Kafka->Ignite stream start");
    }

    /**
     * Method finishing data streaming.
     */
    public void streamStop(){

        System.out.println("Kafka->Ignite stream stop");
        // stop on shutdown
        kafkaStreamer.stop();
        igniteStream.close();


        System.out.println(" Written records to cache "+ igniteStream.cacheName()+" : "+ igniteInstance.getOrCreateCache(targetIgniteCacheName).size());

    }

}
