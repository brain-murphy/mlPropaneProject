package algorithms.clusterers;

import datasets.DataSet;
import datasets.Instance;
import datasets.SupervisedWekaParser;
import weka.clusterers.EM;

import java.util.HashMap;
import java.util.Map;

public class EmClusterer implements Clusterer {

    public static String KEY_CLUSTER_COUNT = "cluster count param";
    public static int EXECUTION_SLOT_COUNT = 2;

    private EM em;
    private Map<String, Object> params;

    private Instance[] instances;
    private SupervisedWekaParser wekaParser;


    public static Map<String, Object> createParams(int clusterCount) {
        Map<String, Object> params = new HashMap<>();

        params.put(KEY_CLUSTER_COUNT, clusterCount);

        return params;
    }

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public void train(DataSet dataset) {
        em = new EM();

        setOptions();

        instances = dataset.getInstances();
        wekaParser = new SupervisedWekaParser(dataset);

        try {
            em.buildClusterer(wekaParser.getDataSetAsInstances());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOptions() {
        em.setNumExecutionSlots(EXECUTION_SLOT_COUNT);

        if (params.containsKey(KEY_CLUSTER_COUNT)) {
            try {
                em.setNumClusters((Integer) params.get(KEY_CLUSTER_COUNT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object evaluate(Object input) {
        try {
            return em.clusterInstance(wekaParser.parseInstanceForEvaluation((double[]) input));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Instance, Integer> getClusters() {
        Map<Instance, Integer> clusterings = new HashMap<>(instances.length);

        return null;
    }
}
