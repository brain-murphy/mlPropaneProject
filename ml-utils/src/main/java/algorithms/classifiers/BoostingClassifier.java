package algorithms.classifiers;

import datasets.*;
import algorithms.parsers.SupervisedWekaParser;
import datasets.Instance;
import weka.classifiers.meta.*;
import weka.core.*;

import java.util.*;
import java.util.zip.ZipException;

public class BoostingClassifier implements Classifier {

    public static final String KEY_ALGORITHM_CLASS_NAME = "algorithm class param";
    public static final String KEY_ITERATIONS = "iterations param";

    public static Map<String, Object> createParams(String baseLearner, int maxIterations) {
        Map<String, Object> params = new HashMap<>();

        params.put(KEY_ALGORITHM_CLASS_NAME, baseLearner);
        params.put(KEY_ITERATIONS, maxIterations);

        return params;
    }

    private SupervisedWekaParser parser;
    private weka.classifiers.Classifier booster;
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
    public void train(DataSet<Instance> dataset) {
        parser = new SupervisedWekaParser(dataset);

        if (dataset.hasDiscreteOutput()) {
            createAdaBoost(options);
        } else {
            instantiateAdditiveRegression(options);
        }


        try {
            booster.buildClassifier(parser.getDataSetAsInstances());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void instantiateAdditiveRegression(String[] options) {
        AdditiveRegression regression = new AdditiveRegression();
        try {
            regression.setOptions(options.clone());
        } catch (Exception ze) {

            System.out.println(ze.getClass().toString());

            if (!(ze instanceof ZipException)) {
                throw new RuntimeException(ze);
            }
        }
        booster = regression;
    }

    private void createLogitBoost(String[] options) {
        LogitBoost logitBoost = new LogitBoost();
        try {
            logitBoost.setOptions(options.clone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        booster = logitBoost;
    }

    private void createAdaBoost(String[] options) {
        AdaBoostM1 adaBoost = new AdaBoostM1();
        try {
            adaBoost.setOptions(options.clone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        booster = adaBoost;
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
