package algorithms;

import datasets.*;
import weka.classifiers.*;
import weka.classifiers.meta.*;
import weka.core.*;

import java.util.*;

public class BoostingAlgorithm implements Algorithm {

    public static final String KEY_ALGORITHM_CLASS_NAME = "algorithm class param";
    public static final String KEY_ITERATIONS = "iterations param";

    private WekaParser parser;
    private Classifier booster;
    private String[] options;

    @Override
    public void setParams(Map<String, Object> params) {
        String algorithmClassName = (String) params.get(KEY_ALGORITHM_CLASS_NAME);
        int iterations;
        if (params.containsKey(KEY_ITERATIONS)) {
            iterations = (int) params.get(KEY_ITERATIONS);
        } else {
            iterations = 10; // default
        }
        try {
            options = Utils.splitOptions("-W " + algorithmClassName + " -I " + iterations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void train(DataSet dataset) {
        parser = new WekaParser(dataset);

        booster = new AdaBoostM1();

        try {
            booster.setOptions(options.clone());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            booster.buildClassifier(parser.getDataSetAsInstances());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object evaluate(Object input) {
        try {
            return booster.classifyInstance(parser.parseInstanceForEvaluation((double []) input));
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Cannot classify");
    }
}
