package algorithms;

import datasets.*;
import org.junit.*;

import java.util.*;

public class TestRbfAlgorithm {

    private RbfAlgorithm rbfAlgorithm;

    @Before
    public void setUp() {
        rbfAlgorithm = new RbfAlgorithm();
    }

    @Test
    public void testSetParams() {
        setParams();
    }

    private void setParams() {
        Map<String,Object> params = new HashMap<>();

        rbfAlgorithm.setParams(params);
    }

    @Test
    public void testTrain() {
        setParams();

        rbfAlgorithm.train(new PropaneDataReader().getPropaneDataSet());
    }

    @Test
    public void testSanity() {
        setParams();

        rbfAlgorithm.train(new PropaneDataReader().getPropaneDataSet());

        double output = (double) rbfAlgorithm.evaluate(TestPropaneInstance.generateTestPropaneInstance().getInput());

        Assert.assertTrue("output insane: " + output, output > 15 && output < 40);
    }
}
