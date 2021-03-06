package algorithms.classifiers;

import datasets.*;
import org.encog.ml.data.*;
import org.encog.ml.data.basic.*;
import org.encog.ml.svm.*;
import org.encog.ml.svm.training.*;
import org.encog.util.arrayutil.*;

import java.util.*;

public class SvmClassifier implements Classifier {

    public static final String KEY_KERNEL_TYPE = "kernel type param";
    public static final String KEY_C = "c param";
    public static final String KEY_GAMMA = "gamma param";
    private NormalizeArray inputNormalizer;

    public static Map<String, Object> createParams(Kernel kernel, double c, double gamma) {
        Map<String, Object> params = new HashMap<>();

        params.put(KEY_KERNEL_TYPE, kernel);

        params.put(KEY_GAMMA, gamma);

        params.put(KEY_C, c);

        return params;
    }

    private double[][] input;
    private double[][] output;

    private NormalizedField outputNormalizer;
    private KernelType kernelType;
    private double c;
    private double gamma;

    private SVM svm;


    @Override
    public void setParams(Map<String, Object> params) {
        if (params.containsKey(KEY_C)) {
            c = (double) params.get(KEY_C);
        } else {
            c = 0.1;
        }

        if (params.containsKey(KEY_GAMMA)) {
            gamma = (double) params.get(KEY_GAMMA);
        } else {
            gamma = 0.1;
        }

        if (params.containsKey(KEY_KERNEL_TYPE)) {
            kernelType = ((Kernel) params.get(KEY_KERNEL_TYPE)).getKernelType();
        } else {
            kernelType = KernelType.RadialBasisFunction;
        }
    }

    @Override
    public void setParams(Params params) {
        SvmParams svmParams = (SvmParams) params;

        kernelType = svmParams.kernelType.getKernelType();
        c = svmParams.c;
        gamma = svmParams.gamma;
    }

    @Override
    public void train(DataSet dataset) {
        parseTrainingData(dataset);

        int numInputs = dataset.getInstances()[0].getInput().length;

        SVMType type = dataset.hasDiscreteOutput() ? SVMType.SupportVectorClassification : SVMType.NewSupportVectorRegression;

        svm = new SVM(numInputs, type, kernelType);

        MLDataSet trainingSet = new BasicMLDataSet(input, output);

        final SVMTrain train = new SVMTrain(svm, trainingSet);

        train.setC(c);
        train.setGamma(gamma);

        train.iteration();
        train.finishTraining();
    }

    private void parseTrainingData(DataSet dataSet) {
        setOutputNormalizer(dataSet);

        Instance[] instances = dataSet.getInstances();
        input = new double[instances.length][];
        output = new double[instances.length][];

        inputNormalizer = new NormalizeArray();
        inputNormalizer.setNormalizedLow(kernelType == KernelType.Sigmoid ? 0 : -1);

        for (int i = 0; i < instances.length; i++) {
            input[i] = inputNormalizer.process(instances[i].getInput());
            output[i] = new double[] {outputNormalizer.normalize((instances[i].getOutput()))};
        }
    }

    private void setOutputNormalizer(DataSet dataSet) {
        double[] possibleOutputs = dataSet.getInstances()[0].getPossibleOutputs();

        double normalizedLow;
        if (kernelType == KernelType.Sigmoid) {
            normalizedLow = 0;
        } else {
            normalizedLow = -1;
        }

        outputNormalizer = new NormalizedField(NormalizationAction.Normalize, "outputnormalizer", possibleOutputs[possibleOutputs.length - 1], possibleOutputs[0], 1, normalizedLow);
    }

    @Override
    public Object evaluate(Object input) {
        double[] normalizedInput = inputNormalizer.process((double[]) input);
        double svmResult = svm.compute(new BasicMLData(normalizedInput)).getData()[0];
        double output = outputNormalizer.deNormalize(svmResult);
        return output;
    }

    public enum Kernel {

        Polynomial(KernelType.Poly),
        Linear(KernelType.Linear),
        Sigmoid(KernelType.Sigmoid),
        RadialBasisFunction(KernelType.RadialBasisFunction);

        private KernelType kernelType;

        Kernel(KernelType kernel) {
            kernelType = kernel;
        }

        public KernelType getKernelType() {
            return kernelType;
        }
    }

    public static class SvmParams extends Params {
        private Kernel kernelType;
        private double c;
        private double gamma;

        public SvmParams(Kernel kernelType, double c, double gamma) {
            this.kernelType = kernelType;
            this.c = c;
            this.gamma = gamma;
        }
    }
}
