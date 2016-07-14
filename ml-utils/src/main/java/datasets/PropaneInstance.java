package datasets;

public class PropaneInstance implements Instance {
    private double[] frequencySpectrum;
    private double fuelLevel;

    public PropaneInstance(double[] frequencySpectrum, double fuelLevel) {
        this.frequencySpectrum = frequencySpectrum;
        this.fuelLevel = fuelLevel;
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
        return new double[] {10, 40};
    }

    @Override
    public double getDifference(double y) {
        return y - fuelLevel;
    }
}
