package datasets;


import org.jetbrains.annotations.*;
import util.*;

import java.io.*;
import java.util.*;


public class PropaneDataReader {

    private static final String PROPANE_DATA_2013_FILE_PATH = "datasets/propaneData.ser";
    private static final String PROPANE_DATA_2016_FILE_PATH = "";

    private Map<Float,List<Map<Integer,Integer>>> data;
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
            in.close();
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return data;
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
        throw new RuntimeException("not implemented");
    }

    private PropaneInstance[] parse2016Instances() {
        throw new RuntimeException("not implemented");
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
}
