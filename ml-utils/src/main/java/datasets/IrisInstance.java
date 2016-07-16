package datasets;

import java.util.Arrays;

public class IrisInstance implements Instance {

    private double[] input;
    private double output;

    public IrisInstance(double sepalLength, double sepalWidth, double petalLength, double petalWidth, String species) {
        this(new double[]{sepalLength, sepalWidth, petalLength, petalWidth}, species);
    }

    public IrisInstance(double[] input, String output) {
        this.input = input;
        parseOutput(output);
    }

    protected void parseOutput(String species) {
        switch (species) {
            case "Iris-setosa":
                output = 0;
                break;
            case "Iris-versicolor":
                output = 1;
                break;
            case "Iris-virginica":
                output = 2;
                break;
        }
    }

    private IrisInstance() {
    }

    @Override
    public double[] getInput() {
        return input;
    }

    @Override
    public void setInput(double[] input) {
        this.input = input;
    }

    @Override
    public double getOutput() {
        return output;
    }

    @Override
    public void setOutput(double output) {
        this.output = output;
    }

    @Override
    public double[] getPossibleOutputs() {
        return new double[]{0, 1, 2};
    }

    @Override
    public double getDifference(double y) {
        long classification = Math.round(y);

        if (Math.abs(classification - output) < .1) { // if difference is near zero
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Instance deepCopy() {
        IrisInstance newInstance = new IrisInstance();

        newInstance.setInput(Arrays.copyOf(getInput(), getInput().length));
        newInstance.setOutput(getOutput());

        return newInstance;
    }
}
