package algorithms.classifiers;

import datasets.*;

import java.util.*;

public interface Classifier {
    public void setParams(Map<String, Object> params);
    public void train(DataSet dataset);
    public Object evaluate(Object input);
}
