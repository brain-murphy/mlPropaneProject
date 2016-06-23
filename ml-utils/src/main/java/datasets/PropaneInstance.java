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
    public double getOutput() {
        return fuelLevel;
    }

    @Override
    public double[] getPossibleOutputs() {
        return new double[] {0, 1};
    }

    @Override
    public double getError(double y) {
        if (Math.abs(y - getOutput()) > .1) {
            return 1;
        }
        return 0;
    }
}
