package algorithms.clusterers;

import algorithms.Algorithm;
import algorithms.classifiers.Classifier;
import datasets.DataSet;
import datasets.Instance;

public interface Clusterer extends Algorithm {
    void train(DataSet<Instance> dataSet);

    int[] getClusters();
}
