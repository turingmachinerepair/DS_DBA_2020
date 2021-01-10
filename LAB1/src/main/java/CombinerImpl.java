
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Combiner implementation <br>
 * Combines {@link RecordContainer} and writes total count of each container <br>
 * @author Petrov A.N.
 * @version 1.0
 */
public class CombinerImpl
        extends Reducer<RecordContainer,IntWritable,RecordContainer,IntWritable> {
    private IntWritable result = new IntWritable();

    /**
     * reduce - reduce implementation, counts occurences of pairs (IP,Browser) and records them into context
     * @param key <b>RecordContainer</b> type key, corresponds to one user.
     * @param values counts of occurences
     * @param context combiner context
     */
    public void reduce(RecordContainer key, Iterable<IntWritable> values,
                       Context context
    ) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        result.set(sum);

        context.write(key , result);
    }
}
