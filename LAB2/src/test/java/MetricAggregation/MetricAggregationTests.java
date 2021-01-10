import MetricAggregation.MetricAggregation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MetricAggregationTests {
    MetricAggregation inst;

    @Before
    public void setup(){

        String[] args = {
                "MetricAgg.cfg",
                "data.txt",
                "out" ,
                "1m"
        };
        inst = new MetricAggregation();
        inst.processParameters(args);
    }

    @Test
    public void mainPipeTest(){


        String[] expect = {"(1,1510670880000,1m,30)" , "(2,1510670880000,1m,20)"};
        ArrayList<String> actual = new ArrayList<String>() ;
        inst.exec();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "./out/part-00000"));
            String line = "-";
            while (line != null) {
                //System.out.println(line);
                // read next line
                line = reader.readLine();
                actual.add(line);
            }
            actual.remove(actual.size()-1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertArrayEquals( expect, actual.toArray());
    }

    @Test
    public void testScaleToMillisSinceEpoch(){
        Long expect = 60000L;
        Long act = inst.scaleToMillisSinceEpoch("1m");
        assertEquals(expect,act);
    }
}
