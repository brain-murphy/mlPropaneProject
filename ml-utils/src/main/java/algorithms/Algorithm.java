package algorithms;

import datasets.*;

import java.util.*;

public interface Algorithm {
    public void setParams(Map<String, Object> params);
    public void train(DataSet dataset);
    public Object evaluate(Object input);
}
