package datasets;


import org.apache.commons.csv.*;
import org.jetbrains.annotations.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.*;

import java.io.*;
import java.util.*;


public class PropaneDataReader {

    private static final String PROPANE_DATA_2013_FILE_PATH = "datasets/propaneData.ser";
    private static final String PROPANE_DATA_2016_FILE_PATH = "datasets/propaneData2016.csv";
    public static final int ESTIMATED_2016_INSTANCE_COUNT = 800;
    public static final String PCA_DATA_FILE_PATH = "datasets/allPropaneDataPca.csv";
    public static final String RP_DATA_FILE_PATH = "datasets/RP_propaneData.csv";
    public static final String CSF_DATA_FILE_PATH = "datasets/propane_csfSubset.csv";

    private static final double[] CONTINUOUS_POSSIBLE_OUTPUT_RANGE = new double[] {10, 40};
    private static final double[] DISCRETE_POSSIBLE_OUTPUT_RANGE = new double[] {0, 1};

    private boolean discreteMode = false;

    private Map<Float,List<Map<Integer,Integer>>> data2013; // Map<Weight,List<FFT<Frequency,Magnitude>>>
    private float[] weights;

    public PropaneDataReader() {
        data2013 = deserializePropaneData();

        weights = MLUtils.INSTANCE.toPrimitiveFloatArray(data2013.keySet());
    }

    public PropaneDataReader(boolean discreteMode) {
        this();
        this.discreteMode = discreteMode;
    }

    private Map<Float,List<Map<Integer,Integer>>> deserializePropaneData() {
        Map<Float,List<Map<Integer,Integer>>> data = null;
        try {

            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPANE_DATA_2013_FILE_PATH);
            ObjectInputStream in = new ObjectInputStream(inputStream);
            data = (Map<Float,List<Map<Integer,Integer>>>) in.readObject();
            stripLastFrequency(data);
            in.close();
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }

    private void stripLastFrequency(Map<Float,List<Map<Integer,Integer>>> data) {
        for (List<Map<Integer,Integer>> fftList : data.values()) {
            for (Map<Integer, Integer> fft : fftList) {
                fft.remove(Collections.max(fft.keySet()));
            }
        }
    }

    public DataSet<Instance> getPropaneDataSet() {
        return new DataSet<>(parseCsv("datasets/allPropaneData.csv"), discreteMode);
    }

    public DataSet<PropaneInstance> get2013PropaneDataSet() {
        PropaneInstance[] instances = parse2013Instances();

        return new DataSet<>(instances, false);
    }

    @NotNull
    private PropaneInstance[] parse2013Instances() {
        PropaneInstance[] instances = new PropaneInstance[getFftCount()];

        int instanceIndex = 0;

        for (float weight : weights) {
            for (Map<Integer,Integer> fft : data2013.get(weight)) {
                double propaneLevel = weight;
                instances[instanceIndex] = new PropaneInstance(mapToDoubleArray(fft), propaneLevel, CONTINUOUS_POSSIBLE_OUTPUT_RANGE);
                instanceIndex += 1;
            }
        }
        return instances;
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

    private int getFftCount() {
        int total = 0;
        for (float weight : weights) {
            total += data2013.get(weight).size();
        }

        return total;
    }

    public Integer[] getFrequencies() {
        Map<Integer,Integer> firstFft = data2013.get(weights[0]).get(0);

        Integer[] frequencies = firstFft.keySet().toArray(new Integer[firstFft.keySet().size()]);

        Arrays.sort(frequencies);

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

    private double[] getOutputRange() {
        if (discreteMode) {
            return DISCRETE_POSSIBLE_OUTPUT_RANGE;
        } else {
            return CONTINUOUS_POSSIBLE_OUTPUT_RANGE;
        }
    }

    public DataSet<Instance>[] getReducedDataSets() {
        return new DataSet[]{getPcaDataSet(), getIcaDataSet(), getRpDataSet(), getCsfDataSet()};
    }

    public DataSet<Instance> getClusteredDataSet() {
        throw new NotImplementedException();
    }
}
