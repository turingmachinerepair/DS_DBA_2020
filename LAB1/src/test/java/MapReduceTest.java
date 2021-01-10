
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * MapReduce test - gets three records, for three users - two for crawler, one for mobile safari client.
 */

public class MapReduceTest {
    private ReducerImpl DUTReducer;
    private MapperImpl DUTMapper;

    private MapReduceDriver<Object, Text, RecordContainer, IntWritable, Text, IntWritable> DUTMapRedDriver;


    @Before
    public void setup(){
        DUTMapper = new MapperImpl();
        DUTReducer = new ReducerImpl();

        DUTMapRedDriver = MapReduceDriver.newMapReduceDriver(DUTMapper,DUTReducer);
    }

    /**
     * MapReduce test - gets three records, for three users - two for crawler, one for mobile safari client.
     * @throws IOException
     */
    @Test
    public void mapredTest() throws IOException {
        Text t1 = new Text("99.168.127.53 - - [20/May/2010:07:34:13 +0100] \"GET /media/img/m-inact.gif HTTP/1.1\" 200 2571 \"http://www.example.com/\" \"Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_1_3 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7E18 Safari/528.16\"");
        Text t2 = new Text("67.195.114.50 - - [20/May/2010:07:35:27 +0100] \"GET /post/261556/ HTTP/1.0\" 404 15 \"-\" \"Mozilla/5.0 (compatible; Yahoo! Slurp/3.0; http://help.yahoo.com/help/us/ysearch/slurp)\"");
        Text t3 = new Text("99.168.127.53 - - [20/May/2010:07:34:13 +0100] \"GET /media/img/m-inact.gif HTTP/1.1\" 200 2571 \"-\" \"Mozilla/5.0 (compatible; Yahoo! Slurp/3.0; http://help.yahoo.com/help/us/ysearch/slurp)\"");

        DUTMapRedDriver
                .withInput(new LongWritable(), new Text(t1))
                .withInput(new LongWritable(), new Text(t1))
                .withInput(new LongWritable(), new Text(t2))
                .withInput(new LongWritable(), new Text(t3))
                .withOutput(new Text("Mobile Safari"), new IntWritable(1))
                .withOutput(new Text("Robot/Spider"), new IntWritable(2))
                .runTest();
    }

}
