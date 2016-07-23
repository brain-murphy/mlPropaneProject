package algorithms.classifiers;

import datasets.*;
import algorithms.parsers.SupervisedWekaParser;
import datasets.Instance;
import weka.classifiers.functions.*;

import java.util.*;

public class RbfClassifier implements Classifier {

    public static final String KEY_NUM_RBFS = "num rbfs param";
    public static final String KEY_CONJUGATE_GRADIENT_DESCENT = "conjugate gradient descent param";
    public static final String KEY_TOLERANCE = "tolerance param";

    public static final int DEFAULT_NUM_RBFS = 2;
    public static final double DEFAULT_TOLERANCE = 1.0e-6;
    public static final int DEFAULT_NUM_THREADS = 3;

    private RBFRegressor rbf;
    private Map<String, Object> paramsMap;
    private RbfParams params;
    private SupervisedWekaParser parser;

    @Override
    public void setParams(Map<String, Object> params) {
        this.paramsMap = params;
    }

    @Override
    public void setParams(Params params) {
        this.params = (RbfParams) params;
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

        if (paramsMap != null) {
            setOptionsFromMap();
        } else if (params != null) {
            setOptionsFromParams();
        }
    }

    private void setOptionsFromParams() {
        rbf.setNumFunctions(params.numRbfs);
        rbf.setTolerance(params.tolerance);
        rbf.setUseCGD(params.conjugateGradientDescent);
    }

    private void setOptionsFromMap() {
        if (paramsMap.containsKey(KEY_NUM_RBFS)) {
            rbf.setNumFunctions((Integer) paramsMap.get(KEY_NUM_RBFS));
        } else {
            rbf.setNumFunctions(DEFAULT_NUM_RBFS);
        }

        if (paramsMap.containsKey(KEY_CONJUGATE_GRADIENT_DESCENT)) {
            rbf.setUseCGD((Boolean) paramsMap.get(KEY_CONJUGATE_GRADIENT_DESCENT));
        } else {
            rbf.setUseCGD(false);
        }

        if (paramsMap.containsKey(KEY_TOLERANCE)) {
            rbf.setTolerance((Double) paramsMap.get(KEY_TOLERANCE));
        } else {
            rbf.setTolerance(DEFAULT_TOLERANCE);
        }
    }

    public static class RbfParams extends Params {
        public RbfParams(int numRbfs, double tolerance, boolean conjugateGradientDescent) {
            this.numRbfs = numRbfs;
            this.tolerance = tolerance;
            this.conjugateGradientDescent = conjugateGradientDescent;
        }
        int numRbfs;
        boolean conjugateGradientDescent;
        double tolerance;
    }
}
