package datasets;


import com.sun.crypto.provider.*;
import org.apache.commons.csv.*;
import org.jetbrains.annotations.*;
import util.*;

import java.io.*;
import java.util.*;


public class PropaneDataReader {

    private static final String PROPANE_DATA_2013_FILE_PATH = "datasets/propaneData.ser";
    private static final String PROPANE_DATA_2016_FILE_PATH = "datasets/propaneData2016.csv";
    public static final int ESTIMATED_2016_INSTANCE_COUNT = 800;

    private Map<Float,List<Map<Integer,Integer>>> data; // Map<Weight,List<FFT<Frequency,Magnitude>>>
    private float[] weights;

    public PropaneDataReader() {
        data = deserializePropaneData();

        weights = MLUtils.toPrimitiveFloatArray(data.keySet());
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

    public DataSet<PropaneInstance> getPropaneDataSet() {
        PropaneInstance[] propaneInstances2013 = parse2013Instances();

        PropaneInstance[] propaneInstances2016 = parse2016Instances();

        PropaneInstance[] combinedArrays = new PropaneInstance[propaneInstances2013.length + propaneInstances2016.length];

        System.arraycopy(propaneInstances2013, 0, combinedArrays, 0, propaneInstances2013.length);
        System.arraycopy(propaneInstances2016, 0, combinedArrays, propaneInstances2013.length, propaneInstances2016.length);

        return new DataSet<>(combinedArrays, false);
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
            for (Map<Integer,Integer> fft : data.get(weight)) {
                double propaneLevel = weight;
                instances[instanceIndex] = new PropaneInstance(mapToDoubleArray(fft), propaneLevel);
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
        CSVParser parser = GeneralUtils.getCsvParser(PROPANE_DATA_2016_FILE_PATH);

        Iterator<CSVRecord> iterator = parser.iterator();

        CSVRecord headerLine = iterator.next();

        int inputCount = headerLine.size() - 1;

        ArrayList<PropaneInstance> instances = new ArrayList<>(ESTIMATED_2016_INSTANCE_COUNT);

        while (iterator.hasNext()) {
            CSVRecord csvRecord = iterator.next();

            double[] inputArray = new double[inputCount];

            for (int i = 0; i < inputArray.length; i++) {
                inputArray[i] = Double.parseDouble(csvRecord.get(i));
            }

            double output = Double.parseDouble(csvRecord.get(headerLine.size() - 1));

            if (output > 30 || output < 15) {
                throw new RuntimeException();
            }

            instances.add(new PropaneInstance(inputArray, output));
        }

        return instances.toArray(new PropaneInstance[instances.size()]);
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
            total += data.get(weight).size();
        }

        return total;
    }

    public Integer[] getFrequencies() {
        Map<Integer,Integer> firstFft = data.get(weights[0]).get(0);

        Integer[] frequencies = firstFft.keySet().toArray(new Integer[firstFft.keySet().size()]);

        Arrays.sort(frequencies);

        return frequencies;
    }
}
