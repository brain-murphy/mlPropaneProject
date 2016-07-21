package algorithms.filters;

import algorithms.Algorithm;
import datasets.*;

import java.util.*;

public interface Filter extends Algorithm {
    DataSet<Instance> filterDataSet(DataSet<Instance> input);
    Instance filterInstance(Instance instance);
}
