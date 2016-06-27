package algorithms;

import datasets.*;

import java.util.*;

public interface IterativeAlgorithm extends Algorithm {
    public void setParams(Map<String, Object> params);

    public void train(DataSet dataset);

    public double step();

    public Object evaluate(Object input);
}
