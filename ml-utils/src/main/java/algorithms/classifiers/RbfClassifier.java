package algorithms.classifiers;

import datasets.*;
import algorithms.parsers.SupervisedWekaParser;
import weka.classifiers.functions.*;
import weka.core.*;

import java.util.*;

public class RbfClassifier implements Classifier {

    public static final String KEY_NUM_RBFS = "num rbfs param";
    public static final String KEY_CONJUGATE_GRADIENT_DESCENT = "conjugate gradient descent param";
    public static final String KEY_TOLERANCE = "tolerance param";

    private RBFRegressor rbf;
    private Map<String, Object> params;
    private SupervisedWekaParser parser;

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public void train(DataSet dataset) {
        parser = new SupervisedWekaParser(dataset);

        rbf = new RBFRegressor();

        try {
            rbf.setOptions(paramsToOptionsString());
            rbf.buildClassifier(parser.getDataSetAsInstances());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object evaluate(Object input) {
        try {
            return rbf.classifyInstance(parser.parseInstanceForEvaluation((double[]) input));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String[] paramsToOptionsString() {
        int threads = 3;
        int numRbfs = params.containsKey(KEY_NUM_RBFS) ? (int) params.get(KEY_NUM_RBFS) : 2;
        boolean conjugateGradientDescent = params.containsKey(KEY_CONJUGATE_GRADIENT_DESCENT) ? (boolean) params.get(KEY_CONJUGATE_GRADIENT_DESCENT) : false;
        double tolerance = params.containsKey(KEY_TOLERANCE) ? (double) params.get(KEY_TOLERANCE) : 1.0e-6;

        try {
            return Utils.splitOptions("-P " + threads + " -E " + threads + (conjugateGradientDescent ? " -G " : "") + " -L " + tolerance + " -N " + numRbfs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
