package algorithms.clusterers;

import algorithms.Algorithm;
import datasets.DataSet;
import datasets.WekaParser;
import weka.clusterers.SimpleKMeans;
import weka.core.MinkowskiDistance;

import java.util.HashMap;
import java.util.Map;

public class KMeansAlgorithm implements Algorithm {

    public static final int NUM_EXECUTION_SLOTS = 2;
    public static double MANHATTAN_DISTANCE_FUNCTION = 1;
    public static double EUCLIDEAN_DISTANCE_FUNCTION = 2;

    public static String KEY_DISTANCE_FUNCTION = "distance func param";
    public static String KEY_K = "k param";

    private Map<String, Object> params;

    public static Map<String,Object> createParams(int k, double distanceFunction) {
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
    public void train(DataSet dataset) {
        kMeans = new SimpleKMeans();

        parseOptions();

        WekaParser wekaParser = new WekaParser(dataset);

        try {
            kMeans.buildClusterer(wekaParser.getDataSetAsInstances());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseOptions() {

        kMeans.setNumExecutionSlots(NUM_EXECUTION_SLOTS);

        MinkowskiDistance distanceFunction = new MinkowskiDistance();

        if (params.containsKey(KEY_DISTANCE_FUNCTION)) {
            distanceFunction.setOrder((Double) params.get(KEY_DISTANCE_FUNCTION));
        } else {
            distanceFunction.setOrder(EUCLIDEAN_DISTANCE_FUNCTION);
        }

        try {
            kMeans.setDistanceFunction(distanceFunction);
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
        try {
            return kMeans.getAssignments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
