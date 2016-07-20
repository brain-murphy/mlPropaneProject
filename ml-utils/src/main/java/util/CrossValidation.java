package util;

import algorithms.classifiers.Classifier;
import datasets.*;
import org.apache.commons.math3.stat.descriptive.*;

import java.util.*;
import java.util.function.*;

public class CrossValidation {

    private static final int DEFAULT_NUM_FOLDS = 10;

    private Function<Double, Double> errorFunction;
    private int numFolds;
    private DataSet<Instance> dataSet;
    private Classifier classifier;

    public static Result crossValidate(DataSet dataSet, int numFolds, Classifier classifier, Function<Double, Double> errorFunction) {
        List<Instance>[] groups = dataSet.splitDataSetRandomly(numFolds);

        SummaryStatistics validationErrors = new SummaryStatistics();
        SummaryStatistics trainingErrors = new SummaryStatistics();

        for (int i = 0; i < numFolds; i++) {
            Instance[] testingData = groups[i].toArray(new Instance[groups[i].size()]);

            DataSet<Instance> trainingDataSet = new DataSet<Instance>(combineAllListsExceptOne(groups, i), dataSet.hasDiscreteOutput());

            classifier.train(trainingDataSet);

            double sumOfValidationError = 0;
            double sumOfTrainingError = 0;

            for (Instance testInstance : testingData) {
                double output = (double) classifier.evaluate(testInstance.getInput());
                double validationErr = errorFunction.apply(testInstance.getDifference(output));
                sumOfValidationError += validationErr;
            }

            for (Instance trainingInstance : trainingDataSet.getInstances()) {
                double output = (double) classifier.evaluate(trainingInstance.getInput());
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

        Instance[] combinedArray = new Instance[totalSizeOfAllLists(lists) - lists[listToLeaveOut].size()];

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

    private static int totalSizeOfAllLists(List[] lists) {
        int total = 0;
        for (List list : lists) {
            total += list.size();
        }

        return total;
    }

    public CrossValidation(Function<Double, Double> errorFunction, int numFolds, DataSet<Instance> dataSet, Classifier classifier) {
        this.errorFunction = errorFunction;
        this.numFolds = numFolds;
        this.dataSet = dataSet;
        this.classifier = classifier;
    }

    public CrossValidation(DataSet<Instance> dataSet, Classifier classifier) {
        this(new AbsoluteError(), DEFAULT_NUM_FOLDS, dataSet, classifier);
    }

    public Result run() {
        return crossValidate(dataSet, numFolds, classifier, errorFunction);
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
