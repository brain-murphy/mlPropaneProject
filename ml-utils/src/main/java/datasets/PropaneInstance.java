package datasets;

import java.util.Arrays;

public class PropaneInstance implements Instance {
    private double[] frequencySpectrum;
    private double fuelLevel;
    private double[] possibleOutputs;

    public PropaneInstance(double[] frequencySpectrum, double fuelLevel, double[] possibleOutputs) {
        this.frequencySpectrum = frequencySpectrum;
        this.fuelLevel = fuelLevel;
        this.possibleOutputs = possibleOutputs;
    }

    @Override
    public double[] getInput() {
        return frequencySpectrum;
    }

    @Override
    public void setInput(double[] input) {
        this.frequencySpectrum = input;
    }

    @Override
    public double getOutput() {
        return fuelLevel;
    }

    @Override
    public void setOutput(double output) {
        this.fuelLevel = output;
    }

    @Override
    public double[] getPossibleOutputs() {
        return possibleOutputs;
    }

    @Override
    public double getDifference(double y) {
        return y - fuelLevel;
    }

    @Override
    public Instance deepCopy() {
        return new PropaneInstance(Arrays.copyOf(getInput(), getInput().length), getOutput(), possibleOutputs);
    }
}
