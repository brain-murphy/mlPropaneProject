package algorithms.classifiers;

import datasets.*;
import algorithms.parsers.SupervisedWekaParser;
import org.apache.commons.math3.analysis.function.Abs;
import weka.Run;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.trees.*;
import weka.core.Instance;

import java.util.*;

public class DecisionTreeClassifier implements Classifier {

    public static final String KEY_TREE_TYPE = "tree type param";

    public static Map<String, Object> createParams(Type treeType) {
        Map<String, Object> params = new HashMap<>();

        params.put(KEY_TREE_TYPE, treeType);

        return params;
    }

    private SupervisedWekaParser parser;
    private AbstractClassifier tree;
    private Map<String, Object> params;


    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public void train(DataSet dataset) {
        parser = new SupervisedWekaParser(dataset);

        tree = configureTree();

        try {
            tree.buildClassifier(parser.getDataSetAsInstances());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private AbstractClassifier configureTree() {
        Type treeType = (Type) params.get(KEY_TREE_TYPE);

        try {
            return (AbstractClassifier) treeType.wekaClassifier.newInstance();
        } catch (Exception e) {
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

    public enum Type {
        DecisionStump(weka.classifiers.trees.DecisionStump.class),
        C45(weka.classifiers.trees.J48.class),
        RepTree(weka.classifiers.trees.REPTree.class),
        LmTree(weka.classifiers.trees.LMT.class);

        private Class wekaClassifier;

        Type(Class classifier) {
            wekaClassifier = classifier;
        }
    }
}
