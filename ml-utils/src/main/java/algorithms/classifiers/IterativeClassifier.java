package algorithms.classifiers;

import datasets.*;

import java.util.*;

public interface IterativeClassifier extends Classifier {
    public void setParams(Map<String, Object> params);

    public void train(DataSet dataset);

    public double step();

    public Object evaluate(Object input);
}
