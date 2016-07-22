package analysis.statistical.errorfunction;

public class RunningAverage {
    private static final int BUFFER_SIZE = 50;

    private int numEntriesRecorded = 0;
    private double currentSum = 0.0;

    public void record(Double number) {
        currentSum += number;
        numEntriesRecorded += 1;
    }

    public double getAverage() {
        return currentSum / numEntriesRecorded;
    }
}
