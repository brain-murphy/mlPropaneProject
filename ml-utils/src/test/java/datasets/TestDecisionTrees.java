package datasets;

import algorithms.classifiers.DecisionTreeClassifier;
import datasets.DataSet;
import datasets.Instance;
import datasets.IrisDataReader;
import org.junit.Before;
import org.junit.Test;

public class TestDecisionTrees {

    private DataSet<Instance> irisDataSet = new IrisDataReader().getIrisDataSet();

    private DecisionTreeClassifier tree;

    @Before
    public void setUp() {
        tree = new DecisionTreeClassifier();
    }

    @Test
    public void testTreeTypeInstantiation() {
        DecisionTreeClassifier.Type[] types = DecisionTreeClassifier.Type.values();

        for (DecisionTreeClassifier.Type treeType : types) {
            tree.setParams(DecisionTreeClassifier.createParams(treeType));

            tree.train(irisDataSet);
        }
    }
}
