package datasets;

import org.apache.commons.csv.*;
import util.*;

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;

public class PcaPropaneDataReader {

    private static final String PROPANE_DATA_2013_FILE_PATH = "datasets/propanedataPca95.csv";
    private static final String PROPANE_DATA_2016_FILE_PATH = "";

    private PropaneInstance[] data;

    public PcaPropaneDataReader() {
        List<PropaneInstance> instanceList = new ArrayList<>();

        CSVParser parser = GeneralUtils.getCsvParser(PROPANE_DATA_2013_FILE_PATH);
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
}
