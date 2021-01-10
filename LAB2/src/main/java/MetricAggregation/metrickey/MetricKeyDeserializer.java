package MetricAggregation.metrickey;

import org.apache.kafka.common.serialization.Deserializer;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * MetricKey class deserializer.
 * Needed for Kafka consumer.
 * @author  Petrov A.N.
 * @version  1.0
 */
public class MetricKeyDeserializer implements Deserializer<MetricKey> {

    /**
     * Object deserialization method implementation
     * @param arg0 -
     * @param arg1 bytes supposedly containing MetricKey instance.
     * @return MetricKey instance
     */
    public MetricKey deserialize(String arg0, byte[] arg1) {
        ObjectMapper mapper = new ObjectMapper();
        MetricKey user = null;
        try {
            user = mapper.readValue(arg1, MetricKey.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}