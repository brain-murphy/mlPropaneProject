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
    public void setParams(Params params) {

    }

    @Override
    public void train(DataSet<Instance> dataset) {
        parser = new SupervisedWekaParser(dataset);

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
