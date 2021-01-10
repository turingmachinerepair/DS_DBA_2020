import nl.bitwalker.useragentutils.UserAgent;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * Reduce test - test class for reducer.
 */

public class ReducerImplTest {

    private ReducerImpl DUTReducer;
    private ReduceDriver<RecordContainer, IntWritable, Text,IntWritable> DUTReducerDriver;

    @Before
    public void theBigComeUp(){
        DUTReducer = new ReducerImpl();
        DUTReducerDriver = DUTReducerDriver.newReduceDriver(DUTReducer);
    }

    /**
     * Reduce test - gets two inputs - one for 192.0.0.1 Firefox user with 5 occurences and one for 192.0.0.2 Firefox user with 10 occurences.
     * Expects Firefox, 2 - corresponding to two users of Firefox
     * @throws IOException
     */

    @Test
    public void testReduce() throws IOException {
        RecordContainer R1 = new RecordContainer( new Text("192.0.0.1"), new Text("Firefox"));
        RecordContainer R2 = new RecordContainer( new Text("192.0.0.2"), new Text("Firefox"));
        List<IntWritable> values = new ArrayList<IntWritable>();
        List<IntWritable> values2 = new ArrayList<IntWritable>();
        int control = 5;
        values.add(new IntWritable(control));
        values2.add(new IntWritable(control*2));

        DUTReducerDriver
                .withInput(R1, values ).withInput(R2,values2)
                .withOutput(new Text("Firefox"),new IntWritable(2))
                .runTest();

    }

}