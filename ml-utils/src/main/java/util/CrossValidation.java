package util;

import algorithms.*;
import datasets.*;
import org.apache.commons.math3.stat.descriptive.*;

import java.util.*;
import java.util.function.*;

public class CrossValidation {

    private static final int DEFAULT_NUM_FOLDS = 10;

    private Function<Double, Double> errorFunction;
    private int numFolds;
    private DataSet<Instance> dataSet;
    private Algorithm algorithm;

    public static Result crossValidate(DataSet dataSet, int numFolds, Algorithm algorithm, Function<Double, Double> errorFunction) {
        List<Instance>[] groups = dataSet.splitDataSetRandomly(numFolds);

        SummaryStatistics validationErrors = new SummaryStatistics();
        SummaryStatistics trainingErrors = new SummaryStatistics();

        for (int i = 0; i < numFolds; i++) {
            Instance[] testingData = groups[i].toArray(new Instance[groups[i].size()]);

            DataSet<Instance> trainingDataSet = new DataSet<Instance>(combineAllListsExceptOne(groups, i), dataSet.hasDiscreteOutput());

            algorithm.train(trainingDataSet);

            double sumOfValidationError = 0;
            double sumOfTrainingError = 0;

            for (Instance testInstance : testingData) {
                double output = (double) algorithm.evaluate(testInstance.getInput());
                sumOfValidationError += errorFunction.apply(testInstance.getDifference(output));
            }

            for (Instance trainingInstance : trainingDataSet.getInstances()) {
                double output = (double) algorithm.evaluate(trainingInstance.getInput());
                sumOfTrainingError += errorFunction.apply(trainingInstance.getDifference(output));
            }

            validationErrors.addValue(sumOfValidationError / testingData.length);
            trainingErrors.addValue(sumOfTrainingError / trainingDataSet.getInstances().length);
        }
        return new Result(validationErrors.getMean(), trainingErrors.getMean(), validationErrors.getStandardDeviation(), numFolds, groups[0].size());
    }



    private static Instance[] combineAllListsExceptOne(List<Instance>[] lists, int listToLeaveOut) {
        if (lists.length == 1) {
            return lists[0].toArray(new Instance[lists[0].size()]);
        }

        Instance[] combinedArray = new Instance[totalCountOfAllLists(lists) - lists[listToLeaveOut].size()];

        int combinedArrayIndex = 0;

        for (int listIndex = 0; listIndex < lists.length; listIndex++) {
            if (listIndex == listToLeaveOut) {
                continue;
            }

            for (Instance instance : lists[listIndex]) {
                combinedArray[combinedArrayIndex] = instance;
                combinedArrayIndex += 1;
            }
        }

        return combinedArray;
    }

    private static int totalCountOfAllLists(List[] lists) {
        int total = 0;
        for (List list : lists) {
            total += list.size();
        }

        return total;
    }

    public CrossValidation(Function<Double, Double> errorFunction, int numFolds, DataSet<Instance> dataSet, Algorithm algorithm) {
        this.errorFunction = errorFunction;
        this.numFolds = numFolds;
        this.dataSet = dataSet;
        this.algorithm = algorithm;
    }

    public CrossValidation(DataSet<Instance> dataSet, Algorithm algorithm) {
        this(new AbsoluteError(), DEFAULT_NUM_FOLDS, dataSet, algorithm);
    }

    public Result run() {
        return crossValidate(dataSet, numFolds, algorithm, errorFunction);
    }

    public void setErrorFunction(Function<Double, Double> errorFunction) {
        this.errorFunction = errorFunction;
    }

    public void setNumFolds(int numFolds) {
        this.numFolds = numFolds;
    }

    public void setDataSet(DataSet<Instance> dataSet) {
        this.dataSet = dataSet;
    }

    public static class SumOfSquaredErrors implements Function<Double, Double> {
        @Override
        public Double apply(Double error) {
            return Math.pow(error, 2);
        }
    }

    public static class AbsoluteError implements Function<Double, Double> {

        @Override
        public Double apply(Double error) {
            return Math.abs(error);
        }
    }
}
