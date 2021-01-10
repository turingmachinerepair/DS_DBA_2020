
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;


/**
 * BrowserStats - application class, implements map-reduce for browser user calculation. <br>
 * "Browser User" - pair of IP and UserAgent. Rationale: if somebody uses Firefox and Chrome on one PC we can call him Firefox user and Chrome user.<br>
 * The aforementioned pair of values is the key in this MapReduce implementation. <br>
 * Map stage is executed by {@link MapperImpl} class, which filters data, converts UserAgent to Browser string and passes data to Reducer. <br>
 * Reducer stage is executed by {@link ReducerImpl} class, which calculates counts of each pair of IP and browser name, then writes them to output file. <br>
 * Combiner stage is executed by {@link CombinerImpl} class, which sums counts of individual pairs of IP and browser. <br>
 * @author Petrov A.N.
 * @version 1.0
 */

public class BrowserStats {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "UserAgent analyzer");

        //class setup
        job.setJarByClass(BrowserStats.class);  //main job class - this superclass
        job.setMapperClass(MapperImpl.class);   //mapper class - MapperImpl
        job.setCombinerClass(CombinerImpl.class); //combiner class - MapperImpl
        job.setReducerClass(ReducerImpl.class); //reducer class - ReducerImpl

        job.setMapOutputKeyClass(RecordContainer.class);
        job.setMapOutputValueClass(IntWritable.class);

        //Output format setup
        job.setOutputKeyClass(Text.class);  //output key - name of browser
        job.setOutputValueClass(IntWritable.class); //output value - how many times browser sent requests
        job.setOutputFormatClass(SequenceFileOutputFormat.class); //output format - sequence file

        //File setup
        FileInputFormat.addInputPath(job, new Path(args[0]));   //input path
        FileOutputFormat.setOutputPath(job, new Path(args[1])); //output path
        FileOutputFormat.setCompressOutput(job,true);   //enable output compression
        //FileOutputFormat.setOutputCompressorClass(job,org.apache.hadoop.io.compress.SnappyCodec.class); //set compression codec to hadoop's Snappy codec
        FileOutputFormat.setOutputCompressorClass(job, org.apache.hadoop.io.compress.SnappyCodec.class);
        job.setNumReduceTasks(1);        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
