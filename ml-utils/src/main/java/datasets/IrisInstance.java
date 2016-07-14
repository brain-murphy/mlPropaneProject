package datasets;

public class IrisInstance implements Instance {

    private double[] input;
    private double output;

    public IrisInstance(double sepalLength, double sepalWidth, double petalLength, double petalWidth, String species) {
        input = new double[]{sepalLength, sepalWidth, petalLength, petalWidth};

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

        if (classification - output < .1) { // if difference is near zero
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Instance newInstance() {
        IrisInstance newInstance = new IrisInstance(0,0,0,0,"");

        newInstance.setInput(getInput());
        newInstance.setOutput(getOutput());

        return newInstance;
    }
}
