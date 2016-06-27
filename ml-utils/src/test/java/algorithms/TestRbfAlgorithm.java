package algorithms;

import datasets.*;
import org.junit.*;

import java.util.*;

public class TestRbfAlgorithm {

    private RbfAlgorithm rbfAlgorithm;
    private double delta;

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

    @Test
    public void testConsistentResult() {
        setParams();

        rbfAlgorithm.train(new PropaneDataReader().getPropaneDataSet());

        double firstOutput = (double) rbfAlgorithm.evaluate(TestPropaneInstance.generateTestPropaneInstance().getInput());
        double secondOutput = (double) rbfAlgorithm.evaluate(TestPropaneInstance.generateTestPropaneInstance().getInput());
        Assert.assertEquals("should be same result", firstOutput, secondOutput, delta);
    }

    @Test
    public void testRestartWithParamChange() {
        setParams();

        rbfAlgorithm.train(new PropaneDataReader().getPropaneDataSet());

        double firstOutput = (double) rbfAlgorithm.evaluate(TestPropaneInstance.generateTestPropaneInstance().getInput());

        Map<String, Object> params = new HashMap<>();

        params.put(RbfAlgorithm.KEY_NUM_RBFS, 3);

        rbfAlgorithm.setParams(params);

        rbfAlgorithm.train(new PropaneDataReader().getPropaneDataSet());

        double secondOutput = (double) rbfAlgorithm.evaluate(TestPropaneInstance.generateTestPropaneInstance().getInput());

        Assert.assertNotEquals("with different params, should be different result", firstOutput, secondOutput,  delta);
    }
}
