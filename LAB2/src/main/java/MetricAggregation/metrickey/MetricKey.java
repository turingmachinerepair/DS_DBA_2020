package MetricAggregation.metrickey;

import scala.Tuple2;
import scala.Tuple3;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Class representing input record key. &lt;metricID, timestamp&gt;
 * @author Petrov A.N.
 * @version 1.0
 */
public class MetricKey implements Serializable {
    /** MetricID
     */
    private Long metricID;
    /** Timestamp, converted to Date class
     */
    private Date timestamp;

    /**
     * Default constructor
     */
    public MetricKey() {
    }

    /**
     * MetricKey constructor
     * @param metricID Date and tim of row
     * @param tstamp hostname
     */
    public MetricKey(Long metricID, Date tstamp) {
        this.metricID = metricID;
        this.timestamp = tstamp;

    }

    /**
     * Metric key constructor
     * @param tuple Tuple with data
     */
    public MetricKey(Tuple2<Long,Long> tuple) {

        this.metricID =  tuple._1();
        this.timestamp =  new Date( tuple._2() );
    }

    /**
     * Return metric ID
     * @return id
     */
    public long getMetricID(){ return metricID; }

    /**
     * Set unique ID
     * @param id metricID
     */
    public void setMetricID(long id){ this.metricID = id; }

    /**
     * Return date and time
     * @return Datetime
     */
    public Date getTimestamp(){
        return timestamp;
    }

    /**
     * Set date and time
     * @param tstamp new timestamp
     */
    public void setTimestamp(Date tstamp){
        timestamp = tstamp;
    }


    /**
     * Returns a string representation of the object
     * @return String representation
     */
    @Override
    public String toString() {
        return String.format("%d %d", metricID , timestamp.getTime());
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param dst Object compare with
     * @return Equals flag
     */
    @Override
    public boolean equals(Object dst) {
        if (this == dst) {
            return true;
        }
        else if (dst == null || getClass() != dst.getClass()){
            return false;
        }
        else {
            MetricKey dstDto = (MetricKey) dst;
            return  metricID.equals(dstDto.metricID) &&
                    timestamp.equals(dstDto.timestamp);
        }

    }

    /**
     * Returns a hash code value for the object.
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(metricID, timestamp);
    }

}
