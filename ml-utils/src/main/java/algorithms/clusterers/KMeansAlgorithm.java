package algorithms.clusterers;

import algorithms.Algorithm;
import datasets.DataSet;
import datasets.Instance;
import datasets.parsers.WekaParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.ManhattanDistance;

import java.util.HashMap;
import java.util.Map;

public class KMeansAlgorithm implements Clusterer {

    public static final int NUM_EXECUTION_SLOTS = 2;

    public static String KEY_DISTANCE_FUNCTION = "distance func param";
    public static String KEY_K = "k param";

    private Map<String, Object> params;
    private Instance[] instances;

    public static Map<String,Object> createParams(int k, DistanceFunction distanceFunction) {
        Map<String,Object> params = new HashMap<>();

        params.put(KEY_K, k);
        params.put(KEY_DISTANCE_FUNCTION, distanceFunction);

        return params;
    }

    private SimpleKMeans kMeans;

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public void train(DataSet dataSet) {
        kMeans = new SimpleKMeans();

        instances = dataSet.getInstances();

        parseOptions();

        WekaParser wekaParser = new WekaParser(dataSet);

        try {
            kMeans.buildClusterer(wekaParser.getDataSetAsInstances());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseOptions() {

        kMeans.setNumExecutionSlots(NUM_EXECUTION_SLOTS);
        kMeans.setPreserveInstancesOrder(true);

        try {
            if (params.containsKey(KEY_DISTANCE_FUNCTION)) {

                kMeans.setDistanceFunction(((DistanceFunction) params.get(KEY_DISTANCE_FUNCTION)).getDistanceFunction());

            } else {
                kMeans.setDistanceFunction(new EuclideanDistance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (params.containsKey(KEY_K)) {
            try {
                kMeans.setNumClusters((Integer) params.get(KEY_K));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object evaluate(Object input) {
        throw new NotImplementedException();
    }

    @Override
    public int[] getClusters() {
        try {
            return kMeans.getAssignments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public enum DistanceFunction {
        Euclidean(new EuclideanDistance()),
        Manhattan(new ManhattanDistance()),
        MostProminentAttributeDistanceFunction(new MostProminentAttributeDistanceFunction());

        private weka.core.DistanceFunction distanceFunction;

        DistanceFunction(weka.core.DistanceFunction distanceFunction) {
            this.distanceFunction = distanceFunction;
        }

        private weka.core.DistanceFunction getDistanceFunction() {
            if (distanceFunction.getInstances() != null) {
                distanceFunction.clean();
            }

            return distanceFunction;
        }
    }
}
