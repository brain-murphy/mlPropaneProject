package datasets;

import org.jetbrains.annotations.*;
import weka.core.*;

public class WekaParser {

    private static final String OUTPUT_ATTRIBUTE_NAME = "output";

    private FastVector attributes;
    private Instances wekaInstances;
    private Instances unlabeledInstances;

    public WekaParser(DataSet<Instance> dataSet) {
        parseData(dataSet);
    }

    private void parseData(DataSet<Instance> dataSet) {
        Instance firstInstance = dataSet.getInstances()[0];

        int numAttributes = firstInstance.getInput().length + 1;

        attributes = new FastVector(numAttributes);

        for (int i = 0; i < firstInstance.getInput().length; i++) {
            attributes.addElement(new Attribute(Integer.toString(i)));
        }

        Attribute outputAttribute = null;
        if (dataSet.hasDiscreteOutput()) {
            outputAttribute = parseDiscreteOutputAttribute(firstInstance.getPossibleOutputs());
        } else {
            outputAttribute = new Attribute(OUTPUT_ATTRIBUTE_NAME);
        }

        attributes.addElement(outputAttribute);

        wekaInstances = new Instances("dataset", attributes, dataSet.getInstances().length);
        unlabeledInstances = new Instances("unlabeled", attributes, dataSet.getInstances().length);

        wekaInstances.setClass(outputAttribute);
        unlabeledInstances.setClass(outputAttribute);

        for (Instance instance : dataSet) {
            weka.core.Instance wekaInstance = new weka.core.Instance(numAttributes);

            for (int i = 0; i < firstInstance.getInput().length; i++) {
                wekaInstance.setValue((Attribute) attributes.elementAt(i), instance.getInput()[i]);
            }

            wekaInstance.setValue(outputAttribute, instance.getOutput());

            wekaInstances.add(wekaInstance);
        }
    }

    @NotNull
    private Attribute parseDiscreteOutputAttribute(double[] possibleOutputs) {
        FastVector outputAttributeValues = new FastVector(possibleOutputs.length);

        for (int i = 0; i < possibleOutputs.length; i++) {
            outputAttributeValues.addElement(Double.toString(possibleOutputs[i]));
        }

        return new Attribute(OUTPUT_ATTRIBUTE_NAME, outputAttributeValues);
    }

    public Instances getDataSetAsInstances() {
        return wekaInstances;
    }

    public weka.core.Instance parseInstanceForEvaluation(double[] input) {
        weka.core.Instance instance = new weka.core.Instance(attributes.size());

        for (int i = 0; i < input.length; i++) {
            instance.setValue((Attribute) attributes.elementAt(i), input[i]);
        }

        unlabeledInstances.add(instance);

        return unlabeledInstances.lastInstance();
    }

    public FastVector getAttributes() {
        return attributes;
    }
}
