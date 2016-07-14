package datasets.parsers;

import datasets.DataSet;
import datasets.Instance;
import datasets.PropaneInstance;
import util.linalg.Matrix;
import util.linalg.Vector;

public class AbagailParser {

    private DataSet<Instance> originalDataSet;

    public shared.DataSet toAbagailDataSet(DataSet<Instance> dataSet) {
        originalDataSet = dataSet;

        Instance[] myInstances = originalDataSet.getInstances();

        shared.Instance[] abagailInstances = new shared.Instance[myInstances.length];


        for (int i = 0; i < myInstances.length; i++) {
            double[] input = myInstances[i].getInput();

            abagailInstances[i] = new shared.Instance(input);
        }

        return new shared.DataSet(abagailInstances);
    }

    public DataSet<Instance> backToMyDataSetFormat(shared.DataSet dataSet) {
        if (originalDataSet == null) {
            throw new IllegalStateException("must parse to ABAGAIL DataSet before calling backToMyDataSetFormat");
        }

        shared.Instance[] abagailInstances = dataSet.getInstances();

        Instance[] myInstances = originalDataSet.getInstances();


        for (int i = 0; i < abagailInstances.length; i++) {
            double[] input = parseVector(abagailInstances[i].getData());

            myInstances[i].setInput(input);
        }

        return originalDataSet;
    }

    private double[] parseVector(Vector vector) {
        double[] array = new double[vector.size()];

        for (int i = 0; i < vector.size(); i++) {
            array[i] = vector.get(i);
        }

        return array;
    }
}
