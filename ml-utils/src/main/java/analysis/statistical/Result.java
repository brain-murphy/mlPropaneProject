package analysis.statistical;

public class Result {
    private double meanValidationError;
    private double meanTrainingError;
    private double validationErrorStDev;
    private int foldCount;
    private int foldSize;

    public Result(double meanValidationError, double meanTrainingError, double validationErrorStDev, int foldCount, int foldSize) {
        this.meanValidationError = meanValidationError;
        this.meanTrainingError = meanTrainingError;
        this.validationErrorStDev = validationErrorStDev;
        this.foldCount = foldCount;
        this.foldSize = foldSize;
    }

    public int getFoldCount() {
        return foldCount;
    }

    public int getFoldSize() {
        return foldSize;
    }

    public double getValidationErrorStDev() {
        return validationErrorStDev;
    }

    public double getMeanTrainingError() {
        return meanTrainingError;
    }

    public double getMeanValidationError() {
        return meanValidationError;
    }
}
