package dimreduction

import algorithms.BoostingAlgorithm
import algorithms.NeuralNetAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.PropaneDataReader
import filters.RandomizedProjectionsWrapper
import learningCurveREPBoosting
import neuralNetLearningCurve
import util.ElapsedTime
import util.MLUtils
import util.absoluteError
import util.timeThis


fun main(args: Array<String>) {
    randomProjectionsWithNNet(PropaneDataReader().propaneDataSet, ::absoluteError)
}

fun randomProjectionsWithBoosting(dataSet: DataSet<Instance>, errorFunction: (Double) -> Double) {
    val booster = BoostingAlgorithm()

    booster.setParams(BoostingAlgorithm.createParams("weka.classifiers.trees.REPTree", 50))

    val randomProjections = RandomizedProjectionsWrapper(dataSet, booster, errorFunction)

    val iterations = 100

    val numFeaturesOut = MLUtils.getNumInputs(dataSet) / 8

    val bestProjectedDataSet = randomProjections.findBestRandomProjection(numFeaturesOut, iterations)


    ElapsedTime.tic()
    System.out.println("original Dataset")
    learningCurveREPBoosting(dataSet, ::absoluteError)
    System.out.println("elapsed time: ${ElapsedTime.toc()}")


    ElapsedTime.tic()
    System.out.println("random projection dataset");
    learningCurveREPBoosting(bestProjectedDataSet, ::absoluteError)
    System.out.println("elapsed time: ${ElapsedTime.toc()}")
}

fun randomProjectionsWithNNet(dataSet: DataSet<Instance>, errorFunction: (Double) -> Double) {
    val nNet = NeuralNetAlgorithm()

    nNet.setParams(NeuralNetAlgorithm.createParams(intArrayOf(9, 8), 0.006f, 500))

    val randomProjections = RandomizedProjectionsWrapper(dataSet, nNet, errorFunction)

    val iterations = 10
    val numFeaturesOut = MLUtils.getNumInputs(dataSet) / 8

    val bestProjectDataSet = randomProjections.findBestRandomProjection(numFeaturesOut, iterations)

    System.out.println("original dataset")
    val originalDataSetTime = timeThis { neuralNetLearningCurve(bestProjectDataSet, errorFunction) }
    System.out.println("elapsed time: $originalDataSetTime")

    System.out.println("random projection dataset")
    val randomProjectionSetTime = timeThis { neuralNetLearningCurve(bestProjectDataSet, errorFunction) }
    System.out.println("elapsed time: $randomProjectionSetTime")
}