package MetricAggregation.metrickey;

import org.apache.kafka.common.serialization.Serializer;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * MetricKey class serializer.
 * Needed for Kafka producer.
 * @author  Petrov A.N.
 * @version  1.0
 */
public class MetricKeySerializer implements Serializer<MetricKey> {

    /**
     * Object serialization method implementation
     * @param arg0 -
     * @param arg1 MetricKey instance, serialization object
     * @return MetricKey converted to bytes
     */
    public byte[] serialize(String arg0, MetricKey arg1) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(arg1).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

}