import java.io.IOException;
import java.util.regex.*;
import java.lang.*;

import nl.bitwalker.useragentutils.Browser;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Mapper implementation <br>
 * Processes text line-by-line. Checks if line contains a valid httpd log record. <br>
 * If if does - extracts UserAgent, converts it to Browser string and constructs {@link RecordContainer}. <br>
 * For each valid line writes pair of {@link RecordContainer} and IntWritable of value 1 to context. <br>
 * Essentially filters invalid strings and creates records for futher processing.
 * @author Petrov A.N.
 * @version 1.0
 */
public class MapperImpl
        extends Mapper<Object, Text, RecordContainer, IntWritable>{

    // log record format - %h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\"
    // matching regex - "^(\\S+) (\\S+) (\\S+) \\[(.+?)\\] \\\"(.+?)\\\" (\\d{3}) (\\S+) \\\"(.+?)\\\" \\\"(.+?)\\\"[\\W]+ $"
    Pattern httpdLogRegex = Pattern.compile("^([\\d.]+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+) \"([^\"]+)\" \"([^\"]+)\"");

    private final static IntWritable one = new IntWritable(1);
    /**
     * isValid - checks if str is a valid httpd log line corresponding to " %h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\" " template
     * @param str input string
     * @return true if <b>str</b> is a valid httpd log line
     */
    boolean isValid(String str){
        return httpdLogRegex.matcher(str).matches();
    }

    /**
     * extractLastQuoted - extract last quoted substring. UserAgent value, if line is a valid httpd log line
     * @param str input strings
     * @return last quoted substring
     */
    String extractLastQuoted(String str){
        int p1 = str.lastIndexOf("\"");
        int i=p1-1;
        while( str.charAt(i) != '\"' ){
            i--;
        }
        return str.substring(i+1,p1);
    }

    /**
     * extractIP - get IP Address from valid httpd log line. Functionaly gets first word
     * @param str input string
     * @return lexicographical difference of <b>IP</b>, if unequal, else - lexicographical difference of <b>UA</b>
     */
    String extractIP(String str){
        int i = str.indexOf(' ');
        String word = str.substring(0, i);
        return word;
    }

    /**
     * validIP - check if IP is valid
     * @param IP input IP
     * @return true if IP is valid, else - false
     */
    public static boolean validIP(String IP){
        String[] nums = IP.split("\\.");
        if( nums.length != 4)
            return false;
         for(String i: nums){
            try{
                int j = Integer.parseInt(i);
                if( j<0 || j>255 )
                    return false;
            } catch(NumberFormatException nfe)  {
                return false;
            }
        }
        return true;
    }

    /**
     * {@link Mapper#map} method implementation.
     * Checks if input value is a valid httpd log line.
     * If it is - extracts IP and UserAgent, converts UserAgent to browser line (Map-Side Join), constructs {@link RecordContainer}.
     * Writes to context writes pair of {@link RecordContainer} and IntWritable of value 1 to context.
     * @param key - key object
     * @param value - line from input data block
     * @param context - Mapper hadoop context
     */
    public void map(Object key, Text value, Context context
    ) throws IOException, InterruptedException {

        String str = value.toString();
        if( isValid(str) ) { //if str is a valid httpd line - extract userAgent and append record to context
            String userAgent = extractLastQuoted(str);
            String exIP = extractIP(str);
            if( validIP(exIP) ){
                Browser bs = Browser.parseUserAgentString(userAgent);
                //System.out.println("Detected"+bs.getName());
                RecordContainer nKey = new RecordContainer( new Text(exIP) , new Text(bs.getName()) );
                context.write(nKey, one);
            }
        }

    }

}