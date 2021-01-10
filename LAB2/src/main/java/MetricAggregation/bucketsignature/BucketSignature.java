package MetricAggregation.bucketsignature;

import scala.Tuple3;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Class representing metric bucket.
 * @author Petrov A.N.
 * @version 1.0
 * @since 1.0
 */
public class BucketSignature implements Serializable {
    /** Metric ID
     */
    private Long metricID;
    /** Timestamp, original timestamp rounded to scale
     */
    private Date timestamp;
    /** Timestamp scale
     */
    private String scale;

    /**
     * Default class constructor
     */
    public BucketSignature() {
    }

    public BucketSignature(BucketSignature sig) {
        this.metricID = sig.metricID;
        this.timestamp = sig.timestamp;
        this.scale=sig.scale;
    }

    /**
     * BucketSignature constructor
     * @param _metricID ID of metric
     * @param _tstamp timestamp
     * @param _scale timestamp scale string
     */
    public BucketSignature(Long _metricID, Date _tstamp, String _scale) {
       this.metricID = _metricID;
       this.timestamp = _tstamp;
       this.scale = _scale;
    }

    /**
     * BucketSignature constructor
     * @param tuple Tuple with data - metricID, timestamp represented in milliseconds since Unix epoch, scale string.
     */
    public BucketSignature(Tuple3<Long,Long,String> tuple) {

        this.metricID =  tuple._1();
        this.timestamp =  new Date( tuple._2() );
        this.scale =  tuple._3();
    }

    /**
     * Return metric ID
     * @return id
     */
    public long getMetricID(){ return metricID; }

    /**
     * Set metric ID
     * @param id metricID long
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
     * @param tstamp timestamp represented by Date class
     */
    public void setTimestamp(Date tstamp){
        timestamp = tstamp;
    }

    /**
     * Scale string getter
     * @return String of scale
     */
    public String getScale(){
        return scale;
    }

    /**
     * Scale string setter
     * @param scale scale, String
     */
    public void setScale(String scale){
        this.scale = scale;
    }

    /**
     * Returns a string representation of the object
     * @return String representation
     */
    @Override
    public String toString() {
        return String.format("%d,%d,%s", metricID , timestamp.getTime(), scale);
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
            BucketSignature dstDto = (BucketSignature) dst;
            return  metricID.equals(dstDto.metricID) &&
                    timestamp.equals(dstDto.timestamp) &&
                    scale.equals(dstDto.scale);
        }

    }

    /**
     * Returns a hash code value for the object.
     * @return Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(metricID, timestamp, scale);
    }

}
