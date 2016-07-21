package algorithms.parsers;

import datasets.*;
import org.jetbrains.annotations.*;
import weka.core.*;

import java.util.*;

public class SupervisedWekaParser extends WekaParser {

    private static final String OUTPUT_ATTRIBUTE_NAME = "output";

    private Attribute outputAttribute;

    public SupervisedWekaParser(DataSet<datasets.Instance> dataSet) {
        super(dataSet);
    }

    @NotNull
    private Attribute parseDiscreteOutputAttribute(double[] possibleOutputs) {
        ArrayList<String> outputAttributeValues = new ArrayList<>(possibleOutputs.length);

        for (int i = 0; i < possibleOutputs.length; i++) {
            outputAttributeValues.add(Double.toString(possibleOutputs[i]));
        }

        return new Attribute(OUTPUT_ATTRIBUTE_NAME, outputAttributeValues);
    }

    protected void createWekaInstances(DataSet<datasets.Instance> dataSet) {
        super.createWekaInstances(dataSet);

        wekaInstances.setClass(outputAttribute);
        unlabeledInstances.setClass(outputAttribute);
    }

    protected ArrayList<Attribute> createAttributes(DataSet<datasets.Instance> dataSet) {
        ArrayList<Attribute> attributesList = super.createAttributes(dataSet);

        setOutputAttribute(dataSet);

        attributesList.add(outputAttribute);

        return attributesList;
    }

    private void setOutputAttribute(DataSet<datasets.Instance> dataSet) {
        datasets.Instance firstInstance = dataSet.getInstances()[0];

        double[] possibleOutputs = firstInstance.getPossibleOutputs();

        if (dataSet.hasDiscreteOutput()) {
            outputAttribute = parseDiscreteOutputAttribute(possibleOutputs);
        } else {
            outputAttribute = new Attribute(OUTPUT_ATTRIBUTE_NAME);
        }
    }

    @NotNull
    protected weka.core.Instance parseInstance(datasets.Instance instance) {
        weka.core.Instance wekaInstance = super.parseInstance(instance);

        wekaInstance.setValue(outputAttribute, instance.getOutput());

        return wekaInstance;
    }
}
