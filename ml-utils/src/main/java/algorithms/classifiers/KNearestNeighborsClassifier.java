package algorithms.classifiers;

import algorithms.Algorithm;
import datasets.*;
import algorithms.parsers.SupervisedWekaParser;
import datasets.Instance;
import weka.classifiers.lazy.*;
import weka.core.*;

import java.util.*;

public class KNearestNeighborsClassifier implements Classifier {

    public static final String KEY_K = "k param";

    private SupervisedWekaParser parser;
    private IBk knn;
    private String[] options;

    private KnnParams params;
    private Map<String, Object> paramsMap;

    @Override
    public void setParams(Map<String, Object> params) {
        paramsMap = params;
    }

    @Override
    public void setParams(Params params) {
        this.params = (KnnParams) params;
    }

    @Override
    public void train(DataSet<Instance> dataset) {
        parser = new SupervisedWekaParser(dataset);

        knn = new IBk();

        setOptions();

        try {
            Instances instances = parser.getDataSetAsInstances();
            knn.buildClassifier(instances);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object evaluate(Object input) {
        try {
            return knn.classifyInstance(parser.parseInstanceForEvaluation((double[]) input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("could not classify");
    }

    private void setOptions() {
        if (paramsMap != null && paramsMap.containsKey(KEY_K)) {
            knn.setKNN((Integer) paramsMap.get(KEY_K));
        } else if (params != null) {
            knn.setKNN(params.k);
        }
    }

    public static class KnnParams extends Params {
        public int k;

        public KnnParams(int k) {
            this.k = k;
        }
    }
}
