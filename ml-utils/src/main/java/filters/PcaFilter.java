package filters;

import datasets.*;
import weka.attributeSelection.*;

import java.util.*;

public class PcaFilter implements Filter {
    private PrincipalComponents principalComponents;

    @Override
    public void setParams(Map<String, Object> params) {

    }

    @Override
    public DataSet filterDataSet(DataSet input) {
        principalComponents = new PrincipalComponents();

//        principalComponents.
        return null;
    }

    @Override
    public Instance filterInstance(Instance instance) {
//        return principalComponents.convertInstance(instance);
        return null;
    }
}
