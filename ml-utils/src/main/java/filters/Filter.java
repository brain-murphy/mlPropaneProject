package filters;

import datasets.*;

import java.util.*;

public interface Filter {
    void setParams(Map<String, Object> params);
    DataSet filterDataSet(DataSet input);
    Instance filterInstance(Instance instance);
}
