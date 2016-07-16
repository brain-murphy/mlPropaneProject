package algorithms;

import datasets.*;
import org.encog.engine.network.activation.*;
import org.encog.ml.data.basic.*;
import org.encog.neural.networks.*;
import org.encog.neural.networks.layers.*;
import org.encog.neural.networks.training.propagation.resilient.*;
import org.encog.util.arrayutil.*;

import java.util.*;

public class NeuralNetAlgorithm implements Algorithm {

    private NormalizeArray inputNormalizer;

    public static Map<String, Object> createParams(int[] structure, float trainingErrorThreshold, int maxIterations) {
        Map<String, Object> params = new HashMap<>();

        params.put(KEY_STRUCTURE, structure);
        params.put(KEY_TARGET_ERROR, trainingErrorThreshold);
        params.put(KEY_MAX_ITERATIONS, maxIterations);

        return params;
    }

    public static final String KEY_TARGET_ERROR = "target error param";
    public static final String KEY_MAX_ITERATIONS = "max iterations param";
    public static final String KEY_STRUCTURE = "networkStructure param";

    private static final long LOGGING_INTERVAL = 1000; // ms
    private float targetError;
    private int maxIterations;


    private int[] networkStructure;

    private BasicNetwork currentNetwork;
    private double[][] input;
    private double[][] output;
    private int epoch;
    private double error;

    private Timer loggingTimer;
    private NormalizedField outputNormalizer;


    @Override
    public void setParams(Map<String, Object> params) {
        targetError = (float) params.get(KEY_TARGET_ERROR);
        maxIterations = (int) params.get(KEY_MAX_ITERATIONS);
        networkStructure = (int[]) params.get(KEY_STRUCTURE);
    }

    @Override
    public void train(DataSet pDataSet) {
        parseTrainingData(pDataSet);

        BasicMLDataSet dataSet = new BasicMLDataSet(input, output);

        currentNetwork = parseNetworkStructure();

        currentNetwork.reset();

        ResilientPropagation trainer = new ResilientPropagation(currentNetwork, dataSet);

        startTimedLogging();

        epoch = 1;
        do {
            trainer.iteration();
            error = trainer.getError();
            epoch++;
        } while (error > targetError && epoch < maxIterations);

        loggingTimer.cancel();
    }

    private BasicNetwork parseNetworkStructure() {
        BasicNetwork network = new BasicNetwork();

        network.addLayer(new BasicLayer(null, true, getInputLayerLength()));

        for (int layer : networkStructure) {
            network.addLayer(new BasicLayer(new ActivationSigmoid(), true, layer));
        }

        network.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));

        network.getStructure().finalizeStructure();

        return network;
    }

    private int getInputLayerLength() {
        return input[0].length;
    }

    private void startTimedLogging() {
        TimerTask loggingTimerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Error " + error + " at nn training iteration " + epoch);
            }
        };
        loggingTimer = new Timer();
        loggingTimer.schedule(loggingTimerTask, LOGGING_INTERVAL, LOGGING_INTERVAL);
    }

    private void parseTrainingData(DataSet dataSet) {
        setOutputNormalizer(dataSet);

        Instance[] instances = dataSet.getInstances();
        input = new double[instances.length][];
        output = new double[instances.length][];

        inputNormalizer = new NormalizeArray();
        inputNormalizer.setNormalizedLow(0);

        for (int i = 0; i < instances.length; i++) {
            input[i] = inputNormalizer.process(instances[i].getInput());
            output[i] = new double[] {outputNormalizer.normalize((instances[i].getOutput()))};
        }
    }

    private void setOutputNormalizer(DataSet dataSet) {
        double[] possibleOutputs = dataSet.getInstances()[0].getPossibleOutputs();

        outputNormalizer = new NormalizedField(NormalizationAction.Normalize, "outputnormalizer", possibleOutputs[possibleOutputs.length - 1], possibleOutputs[0], 1, 0);
    }

    @Override
    public Object evaluate(Object input) {
        double[] normalizedInput = inputNormalizer.process((double[]) input);
        return outputNormalizer.deNormalize(currentNetwork.compute(new BasicMLData(normalizedInput)).getData()[0]);
    }
}
