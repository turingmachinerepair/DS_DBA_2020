
package MetricAggregation.bucketsignature;

import MetricAggregation.metrickey.MetricKey;
import MetricAggregation.bucketsignature.BucketSignature;
import org.junit.Assert;
import org.junit.Test;
import scala.Tuple2;


import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.*;

public class BucketSignatureTests {

    @Test
    public void testGetMetricID(){
        Date dt = Date.from(Instant.now());
        BucketSignature testObject = new BucketSignature(1L,dt,"1m");
        assertEquals(1L,testObject.getMetricID());
    }

    @Test
    public void testGetTimestamp(){
        Date dt = Date.from(Instant.now());
        BucketSignature testObject = new BucketSignature(1L,dt,"1m");
        assertEquals(dt, testObject.getTimestamp());

    }

    @Test
    public void testGetScale(){
        Date dt = Date.from(Instant.now());
        BucketSignature testObject = new BucketSignature(1L,dt,"1m");
        assertEquals("1m", testObject.getScale());
    }

    @Test
    public void testSetMetricID(){
        Date dt = Date.from(Instant.now());
        BucketSignature testObject = new BucketSignature(1L,dt,"1m");
        testObject.setMetricID(2L);
        assertEquals(2L,testObject.getMetricID());
    }

    @Test
    public void testSetTimestamp(){
        Date dt = Date.from(Instant.now());
        try{
            Thread.sleep(1);

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail("Exception thrown");
        }
        Date dt2 = Date.from(Instant.now());

        BucketSignature testObject = new BucketSignature(1L,dt,"1m");
        testObject.setTimestamp(dt2);
        assertEquals(dt2,testObject.getTimestamp());
    }

    @Test
    public void testSetScale(){
        Date dt = Date.from(Instant.now());
        BucketSignature testObject = new BucketSignature(1L,dt,"1m");
        testObject.setScale("2m");
        assertEquals("2m",testObject.getScale());
    }

    /**
     * Equality test.
     */
    @Test
    public void testEquals(){

        Date dt = Date.from(Instant.now());
        BucketSignature testObject = new BucketSignature(1L,dt,"1m");
        BucketSignature testObject2 = new BucketSignature(1L,dt,"1m");
        Boolean cmpres= testObject2.equals(testObject);

        assertTrue(cmpres);

    }

    /**
     * Equality test for non-equal objects.
     */
    @Test
    public void testNotEqualsByScale(){

        Date dt = Date.from(Instant.now());
        BucketSignature testObject = new BucketSignature(1L,dt,"1m");
        BucketSignature testObject2 = new BucketSignature(1L,dt,"2m");
        Boolean cmpres= testObject2.equals(testObject);

        assertFalse(cmpres);

    }
    @Test
    public void testNotEqualsByMetricID(){

        Date dt = Date.from(Instant.now());
        BucketSignature testObject = new BucketSignature(1L,dt,"1m");
        BucketSignature testObject2 = new BucketSignature(2L,dt,"1m");
        Boolean cmpres= testObject2.equals(testObject);

        assertFalse(cmpres);

    }

    @Test
    public void testNotEqualsByTimestamp(){

        Date dt = Date.from(Instant.now());
        try{
            Thread.sleep(1);

        } catch (Exception e){
            e.printStackTrace();
            Assert.fail("Exception thrown");
        }
        Date dt2 = Date.from(Instant.now());

        BucketSignature testObject = new BucketSignature(1L,dt,"1m");
        BucketSignature testObject2 = new BucketSignature(1L,dt2,"1m");
        Boolean cmpres= testObject2.equals(testObject);


        assertFalse(cmpres);

    }


}
