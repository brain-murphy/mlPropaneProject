package datasets;

public interface Instance {
    double[] getInput();
    double getOutput();

    double[] getPossibleOutputs();

    /**
     * @param computedOutput error will be determined relative to this value
     * @return 0 if correct, one if incorrect
     */
    double getError(double computedOutput);
}
