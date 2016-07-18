package algorithms.clusterers;

import algorithms.Algorithm;
import datasets.Instance;

import java.util.Map;

public interface Clusterer extends Algorithm {

    int[] getClusters();
}
