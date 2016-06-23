package algorithms;

import datasets.*;
import weka.classifiers.*;
import weka.classifiers.trees.*;
import weka.core.*;
import weka.core.Instance;

import java.util.*;

public class DecisionTreeAlgorithm implements Algorithm {

    public static final String KEY_PRUNING_CONFIDENCE = "pruning confidence param";
    public static final String KEY_REDUCED_ERROR_PRUNING = "reduced error pruning param";
    public static final String KEY_ONLY_BINARY_SPLITS = "binary splits param";

    private WekaParser parser;
    private Classifier tree;
    private String[] options;


    @Override
    public void setParams(Map<String, Object> params) {
        double pruningConfidence;
        if (params.containsKey(KEY_PRUNING_CONFIDENCE)) {
             pruningConfidence = (double) params.get(KEY_PRUNING_CONFIDENCE);
        } else {
            pruningConfidence = .25;
        }


        boolean binarySplitsOnly;
        if (params.containsKey(KEY_ONLY_BINARY_SPLITS)) {
            binarySplitsOnly = (boolean) params.get(KEY_ONLY_BINARY_SPLITS);
        } else {
            binarySplitsOnly = false;
        }

        boolean reducedErrorPruning = (boolean) params.get(KEY_REDUCED_ERROR_PRUNING);
        try {
            if (reducedErrorPruning) {
                options = Utils.splitOptions("-R" +  (binarySplitsOnly ? " -B" : ""));
            } else {
                options = Utils.splitOptions("-C " + pruningConfidence +  (binarySplitsOnly ? " -B" : ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void train(DataSet dataset) {
        parser = new WekaParser(dataset);

        tree = new J48();

        try {
            tree.setOptions(options.clone());

            tree.buildClassifier(parser.getDataSetAsInstances());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
