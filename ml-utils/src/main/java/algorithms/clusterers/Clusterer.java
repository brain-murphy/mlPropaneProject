package algorithms.clusterers;

import algorithms.classifiers.Classifier;

public interface Clusterer extends Classifier {

    int[] getClusters();
}
