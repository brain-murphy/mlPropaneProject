package datasets;

import org.jetbrains.annotations.*;
import weka.core.*;

import java.util.*;

public class WekaParser {

    private static final String OUTPUT_ATTRIBUTE_NAME = "output";

    private ArrayList<Attribute> attributes;
    private Instances wekaInstances;
    private Instances unlabeledInstances;

    public WekaParser(DataSet<Instance> dataSet) {
        parseData(dataSet);
    }

    private void parseData(DataSet<Instance> dataSet) {
        Instance firstInstance = dataSet.getInstances()[0];

        int numAttributes = firstInstance.getInput().length + 1;

        attributes = new ArrayList<>(numAttributes);

        for (int i = 0; i < firstInstance.getInput().length; i++) {
            attributes.add(new Attribute(Integer.toString(i)));
        }

        Attribute outputAttribute = null;
        if (dataSet.hasDiscreteOutput()) {
            outputAttribute = parseDiscreteOutputAttribute(firstInstance.getPossibleOutputs());
        } else {
            outputAttribute = new Attribute(OUTPUT_ATTRIBUTE_NAME);
        }

        attributes.add(outputAttribute);

        wekaInstances = new Instances("dataset", attributes, dataSet.getInstances().length);
        unlabeledInstances = new Instances("unlabeled", attributes, dataSet.getInstances().length);

        wekaInstances.setClass(outputAttribute);
        unlabeledInstances.setClass(outputAttribute);

        for (Instance instance : dataSet) {
            weka.core.Instance wekaInstance = new DenseInstance(numAttributes);

            for (int i = 0; i < firstInstance.getInput().length; i++) {
                wekaInstance.setValue((Attribute) attributes.get(i), instance.getInput()[i]);
            }

            wekaInstance.setValue(outputAttribute, instance.getOutput());

            wekaInstances.add(wekaInstance);
        }
    }

    @NotNull
    private Attribute parseDiscreteOutputAttribute(double[] possibleOutputs) {
        ArrayList<String> outputAttributeValues = new ArrayList<>(possibleOutputs.length);

        for (int i = 0; i < possibleOutputs.length; i++) {
            outputAttributeValues.add(Double.toString(possibleOutputs[i]));
        }

        return new Attribute(OUTPUT_ATTRIBUTE_NAME, outputAttributeValues);
    }

    public Instances getDataSetAsInstances() {
        return wekaInstances;
    }

    public weka.core.Instance parseInstanceForEvaluation(double[] input) {
        weka.core.Instance instance = new DenseInstance(attributes.size());

        for (int i = 0; i < input.length; i++) {
            instance.setValue((Attribute) attributes.get(i), input[i]);
        }

        unlabeledInstances.add(instance);

        return unlabeledInstances.lastInstance();
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }
}
