package algorithms;

import datasets.*;
import datasets.parsers.SupervisedWekaParser;
import weka.classifiers.trees.*;
import weka.core.Instance;

import java.util.*;

public class DecisionTreeAlgorithm implements Algorithm {

    public static final String KEY_ONLY_BINARY_SPLITS = "binary splits param";

    public static Map<String, Object> createParams(boolean onlyBinarySplits) {
        Map<String, Object> params = new HashMap<>();

        params.put(KEY_ONLY_BINARY_SPLITS, onlyBinarySplits);

        return params;
    }

    private SupervisedWekaParser parser;
    private REPTree tree;
    private Map<String, Object> params;


    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public void train(DataSet dataset) {
        parser = new SupervisedWekaParser(dataset);

        tree = new REPTree();

        try {
            tree.setOptions(parseOptions());

            tree.buildClassifier(parser.getDataSetAsInstances());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String[] parseOptions() {
//        boolean onlyBinarySplits = false;
//        if (params.containsKey(KEY_ONLY_BINARY_SPLITS)) {
//            onlyBinarySplits = (boolean ) params.get(KEY_ONLY_BINARY_SPLITS);
//        }

        return new String[]{};
    }

    @Override
    public Object evaluate(Object input) {
        Instance wekaInstance = parser.parseInstanceForEvaluation((double[]) input);

        try {
            return tree.classifyInstance(wekaInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("could not classify");
    }
}
