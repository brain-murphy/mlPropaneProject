package datasets;


import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.csv.*;
import org.jetbrains.annotations.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.*;

import java.io.*;
import java.util.*;


public class PropaneDataReader {

    private static final String PROPANE_DATA_2013_FILE_PATH = "/datasets/propaneData2013.csv";
    private static final String PROPANE_DATA_2016_FILE_PATH = "/datasets/propaneData2016.csv";
    private static final int ESTIMATED_2016_INSTANCE_COUNT = 800;
    private static final String PCA_DATA_FILE_PATH = "/datasets/allPropaneDataPca.csv";
    private static final String RP_DATA_FILE_PATH = "/datasets/RP_propaneData.csv";
    private static final String CSF_DATA_FILE_PATH = "/datasets/propane_csfSubset.csv";
    private static final String RP_VISUALIZATION_FILE_PATH = "/datasets/Rp5dPropaneData.csv";

    private static final double[] CONTINUOUS_POSSIBLE_OUTPUT_RANGE = new double[] {10, 40};
    private static final double[] DISCRETE_POSSIBLE_OUTPUT_RANGE = new double[] {0, 1};
    public static final String ALL_PROPANE_DATASETS_PACKAGE_PATH = "/datasets/allPropaneData.csv";

    private boolean discreteMode = false;


    public PropaneDataReader() {
    }

    public PropaneDataReader(boolean discreteMode) {
        this();
        this.discreteMode = discreteMode;
    }

    private void stripLastFrequency(Map<Float,List<Map<Integer,Integer>>> data) {
        for (List<Map<Integer,Integer>> fftList : data.values()) {
            for (Map<Integer, Integer> fft : fftList) {
                fft.remove(Collections.max(fft.keySet()));
            }
        }
    }

    public DataSet<Instance> getPropaneDataSet() {
        return new DataSet<>(parseCsv(ALL_PROPANE_DATASETS_PACKAGE_PATH), discreteMode);
    }

    public DataSet<PropaneInstance> get2013PropaneDataSet() {
        PropaneInstance[] instances = parse2013Instances();

        return new DataSet<>(instances, false);
    }

    @NotNull
    private PropaneInstance[] parse2013Instances() {
        return parseCsv(PROPANE_DATA_2013_FILE_PATH);
    }

    public DataSet<PropaneInstance> get2016PropaneDataSet() {
        PropaneInstance[] propaneInstances = parse2016Instances();

        return new DataSet<>(propaneInstances, false);
    }

    private PropaneInstance[] parse2016Instances() {
        return parseCsv(PROPANE_DATA_2016_FILE_PATH);
    }

    @NotNull
    private PropaneInstance[] parseCsv(String filePath) {
        CSVParser parser = GeneralUtils.INSTANCE.getCsvParser(filePath);

        Iterator<CSVRecord> iterator = parser.iterator();

        CSVRecord headerLine = iterator.next();

        int inputCount = headerLine.size() - 1;

        ArrayList<PropaneInstance> instances = new ArrayList<>();

        while (iterator.hasNext()) {
            CSVRecord csvRecord = iterator.next();

            double[] inputArray = new double[inputCount];

            for (int i = 0; i < inputArray.length; i++) {
                inputArray[i] = Double.parseDouble(csvRecord.get(i));
            }

            double output = parseOutput(Double.parseDouble(csvRecord.get(headerLine.size() - 1)));

            double[] outputRange = getOutputRange();

            instances.add(new PropaneInstance(inputArray, output, outputRange));
        }

        try {
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instances.toArray(new PropaneInstance[instances.size()]);
    }

    private double parseOutput(double tankWeight) {
        if (discreteMode) {
            return tankWeight < 21 ? 0 : 1;
        } else {
            return tankWeight;
        }
    }

    private static double[] mapToDoubleArray(Map<Integer, Integer> map) {
        double[] array = new double[map.size()];

        int index = 0;

        for (int frequency : map.keySet()) {
            array[index] = map.get(frequency);
            index += 1;
        }

        return array;
    }

    public Integer[] getFrequencies() {
        CSVParser parser = GeneralUtils.INSTANCE.getCsvParser(PROPANE_DATA_2016_FILE_PATH);

        CSVRecord headerLine = parser.iterator().next();


        int numInputs = headerLine.size() - 1;

        Integer[] frequencies = new Integer[numInputs];

        for (int i = 0; i < numInputs; i++) {
            String label = headerLine.get(i);
            frequencies[i] = Integer.parseInt(label.substring(0, label.length() - 2));
        }

        try {
            parser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return frequencies;
    }

    public DataSet<Instance> getPcaDataSet() {
        return new DataSet<>(parseCsv(PCA_DATA_FILE_PATH), discreteMode);
    }

    public DataSet<Instance> getIcaDataSet() {
        throw new NotImplementedException();
    }

    public DataSet<Instance> getRpDataSet() {
        return new DataSet<>(parseCsv(RP_DATA_FILE_PATH), discreteMode);
    }

    public DataSet<Instance> getCsfDataSet() {
        return new DataSet<>(parseCsv(CSF_DATA_FILE_PATH), discreteMode);
    }

    public void setDiscreteMode(boolean discreteMode) {
        this.discreteMode = discreteMode;
    }

    public DataSet<Instance> getRpForVisualization() {
        return new DataSet<>(parseCsv(RP_VISUALIZATION_FILE_PATH), discreteMode);
    }

    private double[] getOutputRange() {
        if (discreteMode) {
            return DISCRETE_POSSIBLE_OUTPUT_RANGE;
        } else {
            return CONTINUOUS_POSSIBLE_OUTPUT_RANGE;
        }
    }

    public DataSet<Instance>[] getReducedDataSets() {
        @SuppressWarnings("unchecked")
        DataSet<Instance>[] dataSets = new DataSet[]{getPcaDataSet(), getIcaDataSet(), getRpDataSet(), getCsfDataSet()};
        return dataSets;
    }

    public DataSet<Instance> getClusteredDataSet() {
        throw new NotImplementedException();
    }
}
