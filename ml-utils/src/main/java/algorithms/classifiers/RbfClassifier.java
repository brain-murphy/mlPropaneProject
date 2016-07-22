package algorithms.classifiers;

import algorithms.Algorithm;
import datasets.*;
import algorithms.parsers.SupervisedWekaParser;
import datasets.Instance;
import weka.classifiers.functions.*;
import weka.core.*;

import java.util.*;

public class RbfClassifier implements Classifier {

    public static final String KEY_NUM_RBFS = "num rbfs param";
    public static final String KEY_CONJUGATE_GRADIENT_DESCENT = "conjugate gradient descent param";
    public static final String KEY_TOLERANCE = "tolerance param";

    public static final int DEFAULT_NUM_RBFS = 2;
    public static final double DEFAULT_TOLERANCE = 1.0e-6;
    public static final int DEFAULT_NUM_THREADS = 3;

    private RBFRegressor rbf;
    private Map<String, Object> params;
    private SupervisedWekaParser parser;

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public void setParams(Params params) {

    }

    @Override
    public void train(DataSet<Instance> dataset) {
        parser = new SupervisedWekaParser(dataset);

        rbf = new RBFRegressor();

        setOptions();

        try {
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

    private void setOptions() {
        rbf.setNumThreads(DEFAULT_NUM_THREADS);

        if (params.containsKey(KEY_NUM_RBFS)) {
            rbf.setNumFunctions((Integer) params.get(KEY_NUM_RBFS));
        } else {
            rbf.setNumFunctions(DEFAULT_NUM_RBFS);
        }

        if (params.containsKey(KEY_CONJUGATE_GRADIENT_DESCENT)) {
            rbf.setUseCGD((Boolean) params.get(KEY_CONJUGATE_GRADIENT_DESCENT));
        } else {
            rbf.setUseCGD(false);
        }

        if (params.containsKey(KEY_TOLERANCE)) {
            rbf.setTolerance((Double) params.get(KEY_TOLERANCE));
        } else {
            rbf.setTolerance(DEFAULT_TOLERANCE);
        }
    }
}
