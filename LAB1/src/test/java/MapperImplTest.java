
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * MapReduce test - mapper test class
 */

public class MapperImplTest {
    private MapDriver<Object, Text, RecordContainer, IntWritable> DUTMapperDriver;
    private MapperImpl DUTMapper;
    private Text validString;
    private Text invalidString;

    @Before
    public void setup(){
        DUTMapper = new MapperImpl();
        DUTMapperDriver = DUTMapperDriver.newMapDriver(DUTMapper);
        validString = new Text("192.168.4.164 - - [22/Dec/2016:15:19:07 +0300] \"HEAD /DVWA/adminadmin/ HTTP/1.1\" 404 139 \"-\" \"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.16 Safari/537.36\"");
        invalidString = new Text( "124");
    }

    /**
     * Test for {@link MapperImpl#isValid} method.
     */
    @Test
    public void isValidTest(){
       assertTrue( DUTMapper.isValid(validString.toString()) );
       assertFalse( DUTMapper.isValid(invalidString.toString()));
    }

    /**
     * Test for {@link MapperImpl#validIP} method.
     */
    @Test
    public void validIP(){
        assertTrue( DUTMapper.validIP( "192.168.0.1") );
        assertFalse( DUTMapper.validIP( "***") );
        assertFalse( DUTMapper.validIP( "256.0.0.1") );
    }

    /**
     * Test for {@link MapperImpl#extractIP(String)} method.
     */
    @Test
    public void IPExtractTest(){
        assert(DUTMapper.extractIP(validString.toString()).compareTo("192.168.4.164") == 0 );
    }

    /**
     * Test for {@link MapperImpl#extractLastQuoted(String)}} method.
     */
    @Test
    public void UAExtractTest(){
        assert(DUTMapper.extractLastQuoted(validString.toString()).compareTo("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.16 Safari/537.36") == 0 );
    }

    /**
     * Test for mapper.
     * Gets valid string, expects pair of {@link RecordContainer} with values "192.168.4.164","Chrome" and IntWritable of 1.
     */
    @Test
    public void mapperValidTest() throws IOException {
        DUTMapperDriver.withInput(new LongWritable(), new Text(validString))
                .withOutput(
                        new RecordContainer( new Text("192.168.4.164"), new Text("Chrome")),
                        new IntWritable(1))
                .runTest();
    }
}