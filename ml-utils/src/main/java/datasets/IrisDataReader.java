package datasets;

import no.uib.cipr.matrix.sparse.IR;
import org.apache.commons.csv.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.*;
import java.util.*;

public class IrisDataReader {
    private static final String IRIS_FILE_PATH = "./Iris.csv";
    private static final String IRIS_PACKAGE_PATH = "datasets/Iris.csv";

    private IrisInstance[] data;

    public IrisDataReader() {
        List<IrisInstance> instanceList = new ArrayList<>();
        CSVParser parser = getParser();
        Iterator<CSVRecord> iterator = parser.iterator();
        iterator.next(); // header line
        while (iterator.hasNext()) {
            CSVRecord csvRecord = iterator.next();
            IrisInstance irisInstance = new IrisInstance(Double.parseDouble(csvRecord.get(1)),Double.parseDouble(csvRecord.get(2)),Double.parseDouble(csvRecord.get(3)),Double.parseDouble(csvRecord.get(4)),csvRecord.get(5));
            instanceList.add(irisInstance);
        }

        data = instanceList.toArray(new IrisInstance[instanceList.size()]);
    }

    public DataSet<Instance> getIrisDataSet() {
        return new DataSet<>(data, true);
    }

    private CSVParser getParser() {

        ClassLoader classLoader = getClass().getClassLoader();
        URL csvData = classLoader.getResource(IRIS_PACKAGE_PATH);
//        File csvData = new File(IRIS_FILE_PATH);

        try {
            return CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("couldn't create parser");
    }
}
