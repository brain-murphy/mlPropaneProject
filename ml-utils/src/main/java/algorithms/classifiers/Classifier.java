package algorithms.classifiers;

import datasets.*;

import java.util.*;

public interface Classifier {
    void setParams(Map<String, Object> params);
    void train(DataSet<Instance> dataset);
    Object evaluate(Object input);
}
