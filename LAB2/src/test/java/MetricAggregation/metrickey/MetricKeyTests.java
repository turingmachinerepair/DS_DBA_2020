import org.junit.Test;
import org.junit.*;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import org.junit.Test;

import MetricAggregation.metrickey.*;
import scala.Tuple2;
import scala.Tuple3;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import org.apache.kafka.common.serialization.Serializer;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.AssertTrue;

/**
 * MetricKey class test set.
 */
public class MetricKeyTests {

    /**
     * metricID getter test.
     */
    @Test
    public void testGetMetricID(){
        Date dt = Date.from(Instant.now());
        MetricKey testObject = new MetricKey(1L,dt);

        assertEquals(testObject.getMetricID(),1L);
    }

    /**
     * timestamp getter test.
     */
    @Test
    public void testGetTimestamp(){
        Date dt = Date.from(Instant.now());
        MetricKey testObject = new MetricKey(1L,dt);

        assertEquals(testObject.getTimestamp(),dt);
    }

    /**
     * metricID setter test.
     */
    @Test
    public void testSetMetricID(){
        Date dt = Date.from(Instant.now());
        MetricKey testObject = new MetricKey(1L,dt);
        testObject.setMetricID(2L);

        assertEquals(testObject.getMetricID(),2L);
    }

    /**
     * timestamp setter test.
     */
    @Test
    public void testSetTimestamp(){
        Date dt = Date.from(Instant.now());
        MetricKey testObject = new MetricKey(1L,dt);
        try{
            Thread.sleep(1);

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail("Exception thrown");
        }
        Date dt2= Date.from(Instant.now());
        testObject.setTimestamp(dt2);

        assertEquals(testObject.getTimestamp(),dt2);
    }

    /**
     * String conversion test.
     */
    @Test
    public void testToString(){
        Date dt = Date.from(Instant.now());
        MetricKey testObject = new MetricKey(1L,dt);
        Long dtL = dt.getTime();
        String targetStr = "1 "+ dtL.toString();
        assertEquals(testObject.toString(), targetStr );
    }

    /**
     * Equality test.
     */
    @Test
    public void testEquals(){

        Date dt = Date.from(Instant.now());
        MetricKey testObject = new MetricKey(1L,dt);
        MetricKey tObject2 = new MetricKey(new Tuple2<Long,Long>(1L,dt.getTime()) );
        Boolean cmpres= tObject2.equals(testObject);

        assertTrue(cmpres);

    }

    /**
     * Equality test for non-equal objects.
     */
    @Test
    public void testNotEquals(){

        Date dt = Date.from(Instant.now());
        MetricKey testObject = new MetricKey(1L,dt);
        MetricKey tObject2 = new MetricKey(new Tuple2<Long,Long>(2L,dt.getTime()) );
        Boolean cmpres= tObject2.equals(testObject);

        assertFalse(cmpres);

    }

    /**
     * MetricKey serializer test.
     */
    @Test
    public void testMetricKeySerialize(){
        MetricKeySerializer desInst = new MetricKeySerializer();
        Date dt = Date.from(Instant.now());
        MetricKey testObject = new MetricKey(1L,dt);
        byte[] testObjectBytes = desInst.serialize(null,testObject);

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] targetBytes = null;
        try{
            targetBytes = objectMapper.writeValueAsString(testObject).getBytes();
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail("Exception thrown");
        }
        assertArrayEquals(testObjectBytes,targetBytes);
    }

    /**
     * MetricKey deserializer test.
     */
    @Test
    public void testMetricKeyDeserialize(){
        MetricKeyDeserializer desInst = new MetricKeyDeserializer();

        //construct test object
        Date dt = Date.from(Instant.now());
        MetricKey testObject = new MetricKey(1L,dt);

        //convert to bytes
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] targetBytes = null;
        try{
            targetBytes = objectMapper.writeValueAsString(testObject).getBytes();
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail("Exception thrown");
        }

        //reconstruct from bytes
        MetricKey deseredObject = desInst.deserialize(null,targetBytes);

        //if objects are equal - assert true
        assertTrue( testObject.equals(deseredObject) );

    }

    /**
     * MetricKey deserializer test with corrupted data.
     */
    @Test
    public void testMetricKeyDeserializeFail(){
        MetricKeyDeserializer desInst = new MetricKeyDeserializer();

        //construct test object
        Date dt = Date.from(Instant.now());
        MetricKey testObject = new MetricKey(1L,dt);

        //convert to bytes
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] targetBytes = null;
        try{
            targetBytes = objectMapper.writeValueAsString(testObject).getBytes();
        } catch (Exception e){
            e.printStackTrace();
            Assert.fail("Exception thrown");
        }
        //corrupt data
        targetBytes[0] = 0;

        //reconstruct from bytes
        MetricKey deseredObject = desInst.deserialize(null,targetBytes);

        //if objects are equal - assert true
        assertNotEquals( deseredObject, testObject );
    }

}
