package algorithms;

import datasets.*;
import weka.classifiers.*;
import weka.classifiers.lazy.*;
import weka.core.*;

import java.util.*;

public class KNearestNeighborsAlgorithm implements Algorithm {

    public static final String KEY_K = "k param";

    private WekaParser parser;
    private Classifier knn;
    private String[] options;

    @Override
    public void setParams(Map<String, Object> params) {
        int k = (int) params.get(KEY_K);
        try {
            options = Utils.splitOptions("-K " + k);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void train(DataSet dataset) {
        parser = new WekaParser(dataset);

        knn = new IBk();

        try {
            knn.setOptions(options.clone());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
}
