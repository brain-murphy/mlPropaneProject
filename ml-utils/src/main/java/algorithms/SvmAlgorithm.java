package algorithms;

import datasets.*;
import org.encog.*;
import org.encog.mathutil.libsvm.*;
import org.encog.ml.data.*;
import org.encog.ml.data.basic.*;
import org.encog.ml.svm.*;
import org.encog.ml.svm.training.*;
import org.encog.util.arrayutil.*;

import java.util.*;

public class SvmAlgorithm implements Algorithm {

    public static final String KEY_OUTPUT_NORMALIZER = "output normalizer param";
    public static final String KEY_KERNEL_TYPE = "kernel type param";
    public static final String KEY_C = "c param";
    public static final String KEY_GAMMA = "gamma param";

    private double[][] input;
    private double[][] output;

    private NormalizedField outputNormalizer;
    private KernelType kernelType;
    private double c;
    private double gamma;

    private SVM svm;


    @Override
    public void setParams(Map<String, Object> params) {
        outputNormalizer = (NormalizedField) params.get(KEY_OUTPUT_NORMALIZER);
        kernelType = (KernelType) params.get(KEY_KERNEL_TYPE);
        c = (double) params.get(KEY_C);
        gamma = (double) params.get(KEY_GAMMA);
    }

    @Override
    public void train(DataSet dataset) {
        parseTrainingData(dataset);

        int numInputs = dataset.getInstances()[0].getInput().length;
        svm = new SVM(numInputs, SVMType.SupportVectorClassification, kernelType);

        MLDataSet trainingSet = new BasicMLDataSet(input, output);

        final SVMTrain train = new SVMTrain(svm, trainingSet);

        train.setC(c);
        train.setGamma(gamma);

        train.iteration();
        train.finishTraining();
    }

    private void parseTrainingData(DataSet dataSet) {
        Instance[] instances = dataSet.getInstances();
        input = new double[instances.length][];
        output = new double[instances.length][];

        for (int i = 0; i < instances.length; i++) {
            input[i] = instances[i].getInput();
            output[i] = new double[] {outputNormalizer.normalize((instances[i].getOutput()))};
        }
    }

    @Override
    public Object evaluate(Object input) {
        return outputNormalizer.deNormalize(svm.compute(new BasicMLData((double[]) input)).getData()[0]);
    }
}
