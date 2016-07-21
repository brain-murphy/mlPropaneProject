package algorithms.classifiers;

import datasets.*;

import java.util.*;

public interface IterativeClassifier extends Classifier {
    void setParams(Map<String, Object> params);

    void train(DataSet dataset);

    double step();

    Object evaluate(Object input);
}
