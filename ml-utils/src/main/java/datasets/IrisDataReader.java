package datasets;

import org.apache.commons.csv.*;
import org.jetbrains.annotations.NotNull;
import util.GeneralUtils;

import java.util.*;

public class IrisDataReader {
    private static final String RP_DATA_PACKAGE_PATH = "datasets/Iris_rp.csv";
    private static final String IRIS_FILE_PATH = "./Iris.csv";
    private static final String IRIS_PACKAGE_PATH = "datasets/Iris.csv";
    private static final String PCA_DATA_PACKAGE_PATH = "datasets/irisPca.csv";
    private static final String ICA_DATA_PACKAGE_PATH = "datasets/Ica_irisData.csv";
    private static final String CSF_DATA_PACKAGE_PATH = "datasets/iris_csfSubset.csv";

    public IrisDataReader() {
    }

    @NotNull
    private IrisInstance[] parseCsv(String filePath) {
        CSVParser parser = GeneralUtils.getCsvParser(filePath);

        Iterator<CSVRecord> iterator = parser.iterator();

        CSVRecord headerLine = iterator.next();

        int inputCount = headerLine.size() - 1;

        ArrayList<IrisInstance> instances = new ArrayList<>();

        while (iterator.hasNext()) {
            CSVRecord csvRecord = iterator.next();

            double[] inputArray = new double[inputCount];

            for (int i = 0; i < inputArray.length; i++) {
                inputArray[i] = Double.parseDouble(csvRecord.get(i));
            }

            String output = csvRecord.get(headerLine.size() - 1);

            instances.add(new IrisInstance(inputArray, output));
        }

        return instances.toArray(new IrisInstance[instances.size()]);
    }

    public DataSet<Instance> getIrisDataSet() {
        return new DataSet<>(parseCsv(IRIS_PACKAGE_PATH), true);
    }

    public DataSet<Instance> getPcaDataSet() {
        return new DataSet<>(parseCsv(PCA_DATA_PACKAGE_PATH), true);
    }

    public DataSet<Instance> getIcaDataSet() {
        return new DataSet<>(parseCsv(ICA_DATA_PACKAGE_PATH), true);
    }

    public DataSet<Instance> getRpDataSet() {
        return new DataSet<>(parseCsv(RP_DATA_PACKAGE_PATH), true);
    }

    public DataSet<Instance> getCsfDataSet() {
        return new DataSet<>(parseCsv(CSF_DATA_PACKAGE_PATH), true);
    }
}
