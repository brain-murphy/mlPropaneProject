package algorithms.classifiers;

import algorithms.Algorithm;
import datasets.*;

public interface Classifier extends Algorithm {
    void train(DataSet<Instance> dataSet);
    Object evaluate(Object input);
}
