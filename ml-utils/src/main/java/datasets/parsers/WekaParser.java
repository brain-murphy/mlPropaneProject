package datasets.parsers;

import datasets.DataSet;
import datasets.Instance;
import org.jetbrains.annotations.NotNull;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;

public class WekaParser {
    private ArrayList<Attribute> attributes;
    private int numInputs;

    protected Instances wekaInstances;
    protected Instances unlabeledInstances;

    public WekaParser(DataSet<Instance> dataSet) {
        Instance firstInstance = dataSet.getInstances()[0];

        numInputs = firstInstance.getInput().length;

        parseData(dataSet);
    }


    private void parseData(DataSet<Instance> dataSet) {
        attributes = createAttributes(dataSet);

        createWekaInstances(dataSet);

        for (Instance instance : dataSet) {
            weka.core.Instance wekaInstance = parseInstance(instance);

            wekaInstances.add(wekaInstance);
        }
    }

    @NotNull
    protected weka.core.Instance parseInstance(Instance instance) {
        weka.core.Instance wekaInstance = new DenseInstance(getAttributes().size());

        for (int i = 0; i < numInputs; i++) {
            wekaInstance.setValue(getAttributes().get(i), instance.getInput()[i]);
        }
        return wekaInstance;
    }

    protected void createWekaInstances(DataSet<Instance> dataSet) {
        wekaInstances = new Instances("dataset", getAttributes(), dataSet.getInstances().length);
        unlabeledInstances = new Instances("unlabeled", getAttributes(), dataSet.getInstances().length);
    }

    protected ArrayList<Attribute> createAttributes(DataSet<Instance> dataSet) {
        ArrayList<Attribute> attributesList = new ArrayList<>();

        for (int i = 0; i < numInputs; i++) {
            attributesList.add(new Attribute(Integer.toString(i)));
        }

        return attributesList;
    }

    public Instances getDataSetAsInstances() {
        return wekaInstances;
    }

    public weka.core.Instance parseInstanceForEvaluation(double[] input) {
        weka.core.Instance instance = new DenseInstance(getAttributes().size());

        for (int i = 0; i < input.length; i++) {
            instance.setValue(getAttributes().get(i), input[i]);
        }

        unlabeledInstances.add(instance);

        return unlabeledInstances.lastInstance();
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }
}
