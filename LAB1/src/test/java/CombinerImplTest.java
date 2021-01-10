
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CombinerImpl test - test class for combiner.
 */

public class CombinerImplTest {

    private CombinerImpl DUTCombiner;
    private ReduceDriver<RecordContainer,IntWritable,RecordContainer,IntWritable> DUTCombinerDriver;

    @Before
    public void setup(){
        DUTCombiner = new CombinerImpl();
        DUTCombinerDriver = DUTCombinerDriver.newReduceDriver(DUTCombiner);
    }

    /**
     * Reduce test - gets three records, returns one record with count 1
     * @throws IOException
     */
    @Test
    public void testReduce() throws IOException {
        RecordContainer R1 = new RecordContainer( new Text("192.0.0.1"), new Text("Firefox"));
        List<IntWritable> values = new ArrayList<IntWritable>();

        values.add(new IntWritable(1));
        values.add(new IntWritable(1));
        values.add(new IntWritable(1));

        DUTCombinerDriver
                .withInput(R1, values )
                .withOutput(R1, new IntWritable( values.size() ) )
                .runTest();
    }


}