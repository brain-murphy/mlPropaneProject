package algorithms;

import datasets.*;
import org.encog.ml.data.basic.*;
import org.encog.neural.networks.*;
import org.encog.neural.networks.training.propagation.resilient.*;
import org.encog.util.arrayutil.*;

import java.util.*;

public class NeuralNetAlgorithm implements Algorithm {

    public static final String KEY_TARGET_ERROR = "target error param";
    public static final String KEY_MAX_ITERATIONS = "max iterations param";
    public static final String KEY_NETWORK = "networkPrototype param";
    public static final String KEY_OUTPUT_NORMALIZER = "output normalizer param";

    private static final long LOGGING_INTERVAL = 1000; // ms
    private float targetError;
    private int maxIterations;


    private BasicNetwork networkPrototype;

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
        networkPrototype = (BasicNetwork) params.get(KEY_NETWORK);
        outputNormalizer = (NormalizedField) params.get(KEY_OUTPUT_NORMALIZER);
    }

    @Override
    public void train(DataSet pDataSet) {
        parseTrainingData(pDataSet);

        BasicMLDataSet dataSet = new BasicMLDataSet(input, output);

        currentNetwork = (BasicNetwork) networkPrototype.clone();
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
        Instance[] instances = dataSet.getInstances();
        input = new double[instances.length][];
        output = new double[instances.length][];

        NormalizeArray inputNormalizer = new NormalizeArray();
        inputNormalizer.setNormalizedLow(0);

        for (int i = 0; i < instances.length; i++) {
            input[i] = inputNormalizer.process(instances[i].getInput());
            output[i] = new double[] {outputNormalizer.normalize((instances[i].getOutput()))};
        }
    }

    @Override
    public Object evaluate(Object input) {
        return outputNormalizer.deNormalize(currentNetwork.compute(new BasicMLData((double[]) input)).getData()[0]);
    }
}
