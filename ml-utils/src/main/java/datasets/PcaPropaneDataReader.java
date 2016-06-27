package datasets;

import org.apache.commons.csv.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

public class PcaPropaneDataReader {

    private static final String IRIS_FILE_PATH = "datasets/propanedataPca95.csv";

    private PropaneInstance[] data;

    public PcaPropaneDataReader() {
        List<PropaneInstance> instanceList = new ArrayList<>();

        CSVParser parser = getParser();
        Iterator<CSVRecord> iterator = parser.iterator();

        iterator.next(); // header line

        while (iterator.hasNext()) {
            CSVRecord csvRecord = iterator.next();

            int numInputs = csvRecord.size() - 1;

            double[] input = new double[numInputs];
            for (int i = 0; i < numInputs; i++) {
                input[i] = Double.parseDouble(csvRecord.get(i));
            }

            double output = Double.parseDouble(csvRecord.get(csvRecord.size() - 1));

            instanceList.add(new PropaneInstance(input, output));
        }

        data = instanceList.toArray(new PropaneInstance[instanceList.size()]);
    }

    public DataSet<PropaneInstance> getPropaneDataSet() {
        return new DataSet<>(data, false);
    }

    private CSVParser getParser() {

        ClassLoader classLoader = getClass().getClassLoader();

        URL csvData = classLoader.getResource(IRIS_FILE_PATH);
//        File csvData = new File(IRIS_FILE_PATH);

        try {
            return CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("couldn't create parser");
    }
}
