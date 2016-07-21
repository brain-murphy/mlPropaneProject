package algorithms.classifiers;

import algorithms.Algorithm;
import datasets.*;

import java.util.*;

public interface Classifier extends Algorithm {
    void train(DataSet<Instance> dataSet);
    Object evaluate(Object input);
}
