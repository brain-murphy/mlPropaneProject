package filters;

import datasets.*;

import java.util.*;

public interface Filter {
    void setParams(Map<String, Object> params);
    DataSet<Instance> filterDataSet(DataSet<Instance> input);
    Instance filterInstance(Instance instance);
}
