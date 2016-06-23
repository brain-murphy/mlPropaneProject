package datasets;


import util.*;

import java.io.*;
import java.util.*;


public class PropaneDataReader {

    private static final String PROPANE_DATA_FILE_PATH = "datasets/propaneData.ser";

    private Map<Float,List<Map<Integer,Integer>>> data;
    private float[] weights;

    public PropaneDataReader() {
        data = deserializePropaneData();

        weights = ProjectUtils.toPrimitiveFloatArray(data.keySet());
    }

    private Map<Float,List<Map<Integer,Integer>>> deserializePropaneData() {
        Map<Float,List<Map<Integer,Integer>>> data = null;
        try {

            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(PROPANE_DATA_FILE_PATH);
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
        PropaneInstance[] instances = new PropaneInstance[getFftCount()];

        int instanceIndex = 0;

        for (float weight : weights) {
            for (Map<Integer,Integer> fft : data.get(weight)) {
                double propaneLevel = weight;
                instances[instanceIndex] = new PropaneInstance(mapToDoubleArray(fft), propaneLevel);
                instanceIndex += 1;
            }
        }

        return new DataSet<>(instances);
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
