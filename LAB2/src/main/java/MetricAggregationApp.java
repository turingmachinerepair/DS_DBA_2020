
import MetricAggregation.MetricAggregation;

/**
 * Top-level class for application.
 * @version 1.0
 * @author Petrov A.N.
 */
public class MetricAggregationApp {

    /**
     * Main function of application.
     * Initializes and executes class with actual logic.
     * @param args - passed arguments
     */
    public static void main(String[] args){
        MetricAggregation inst = new MetricAggregation();
        inst.processParameters(args);
        inst.exec();
    }
}
