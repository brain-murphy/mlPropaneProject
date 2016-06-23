package algorithms;

import datasets.*;
import weka.classifiers.functions.*;

import java.util.*;

public class RbfAlgorithm implements Algorithm {

    private RBFNetwork rbfNetwork;

    @Override
    public void setParams(Map<String, Object> params) {

    }

    @Override
    public void train(DataSet dataset) {
        WekaParser parser = new WekaParser(dataset);

        rbfNetwork = new RBFNetwork();

    }

    @Override
    public Object evaluate(Object input) {
        return null;
    }
}
