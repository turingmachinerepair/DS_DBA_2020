import java.io.IOException;
import java.io.*;
import org.apache.hadoop.io.*;

import nl.bitwalker.useragentutils.Browser;
import org.apache.hadoop.io.Text;

/**
 * Record container <br>
 * Stores pair of IP and browser strings. Corresponds to definition of "browser user"
 * @author Petrov A.N.
 * @version 1.0
 */
public class RecordContainer  implements WritableComparable {
    private Text IP;
    private Text UA;

    /**
     * Default constructor
     */
    public RecordContainer(){
        this.IP = new Text("0.0.0.0");
        this.UA = new Text( Browser.UNKNOWN.getName()  );
    }

    /**
     * Value constructor
     * @param _IP IP address
     * @param _UA Browser name string
     */
    public RecordContainer(Text _IP, Text _UA){
        this.IP=_IP;
        this.UA=_UA;
    }

    /**
     * UA getter
     * @return <b>UA</b> field
     */
    public Text getUA(){
        return this.UA;
    }

    /**
     * write method - writes IP and UA to stream
     * @return {@link RecordContainer#UA} field
     */
    public Text getIP(){
        return this.IP;
    }

    /**
     * Write method - writes IP and UA to stream
     * @param dataOut target output stream
     */
    public void write(DataOutput dataOut) throws IOException {
        IP.write(dataOut);
        UA.write(dataOut);
    }

    /**
     * Read method - reads IP and UA to stream
     * @param dataIn target input stream
     */
    public void readFields(DataInput dataIn) throws IOException {
        IP.readFields(dataIn);
        UA.readFields(dataIn);
    }

    /**
     * hashCode override - writes IP and UA to stream
     * @return hashcode from <b>UA</b> field
     */
    @Override
    public int hashCode(){
        return UA.hashCode();
    }

    /**
     * equals override - equality method
    */
    @Override
    public boolean equals(Object o) {
        if (this == o)
        return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        RecordContainer other = (RecordContainer) o;
        return  IP.equals(other.IP) && UA.equals(other.UA);
    }

    /**
     * toString - cast to string,
     * @return <b>IP</b> and <b>UA</b> concatenation
     */
    @Override
    public String toString(){
        return IP.toString() + "," + UA.toString() ;
    }

    /**
     * compareTo - comparison method implementation
     * Trivial - sequentially lexicographically compares <b>IP</b> and <b>UA</b>
     * @return lexicographical difference of <b>IP</b>, if unequal, else - lexicographical difference of <b>UA</b>
     */
    public int compareTo(Object o) {
        RecordContainer other = (RecordContainer) o;
        int cmp = IP.compareTo( other.getIP() );
        if(  cmp != 0){
                return cmp;
        }
        cmp = UA.compareTo( other.getUA() );
        return cmp;
    }
}
