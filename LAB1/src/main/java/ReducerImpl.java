
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reducer implementation
 * Groups data by users counting frequency of each browser user occurence
 * Writes key-value pairs from Map
 * @author Petrov A.N.
 * @version 1.0
 */

public class ReducerImpl
        extends Reducer<RecordContainer,IntWritable,Text,IntWritable> {

    //dictionary field -  Frequency count by browser
    private TreeMap<String,Integer> dictionary;

    /**
     * setup - setup implementation, initalizes dictionary
     * @param context reducer context
     */
    protected void setup(Context context) throws IOException, InterruptedException{
      dictionary = new TreeMap<String,Integer>();
    }

    /**
     * reduce - reduce implementation, counts occurences of pairs (IP,Browser) and records them into <b>dictionary</b>
     * @param key <b>RecordContainer</b> type key, corresponds to one user.
     * @param values counts of occurences
     * @param context reducer context
     */
    public void reduce(RecordContainer key, Iterable<IntWritable> values,
                       Context context
    ) throws IOException, InterruptedException {

        //get count by pair <IP,Browser>
        /*int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }*/

        //calculate new count for browser and increment by ONE, not by sum since we count different users.
        String browserStr = key.getUA().toString();
        Integer count = 0;
        if( dictionary.get(browserStr) != null){
            System.out.println("Non-zero record for "+browserStr.toString());
            count = dictionary.get(browserStr);
        }

        //put record with new value
        dictionary.put(browserStr, count + new Integer(1));
    }

    /**
     * cleanup - post-reduce method, traverses dictionary and writes key-value pairs into output
     * @param context reducer context
     */
    protected void cleanup(Context context) throws IOException, InterruptedException{
        if(dictionary != null && dictionary.size() >0 ){
            for (Map.Entry<String, Integer> entry : dictionary.entrySet()) {
                context.write( new Text(entry.getKey()) , new IntWritable( entry.getValue() ) );
            }
        }
    }

}
