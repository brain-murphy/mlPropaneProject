package dimreduction

import algorithms.BoostingAlgorithm
import algorithms.NeuralNetAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import datasets.PropaneDataReader
import filters.RandomizedProjectionsWrapper
import learningCurveREPBoosting
import neuralNetLearningCurve
import util.MLUtils
import util.absoluteError
import util.timeThis
import util.writeToFile


fun main(args: Array<String>) {
    val nnetParams: Map<String, Any> = NeuralNetAlgorithm.createParams(intArrayOf(13), 0.0078f, 500)

    randomProjectionsWithNNet(IrisDataReader().irisDataSet, ::absoluteError, nnetParams, 2)
}

fun randomProjectionsWithBoosting(dataSet: DataSet<Instance>, errorFunction: (Double) -> Double, numFeaturesOut: Int) {
    val booster = BoostingAlgorithm()

    booster.setParams(BoostingAlgorithm.createParams("weka.classifiers.trees.REPTree", 50))

    val randomProjections = RandomizedProjectionsWrapper(dataSet, booster, errorFunction)

    val iterations = 100

    val numFeaturesOut = MLUtils.getNumInputs(dataSet) / 8

    val bestProjectedDataSet = randomProjections.findBestRandomProjection(numFeaturesOut, iterations)


    System.out.println("original Dataset")
    val originalDataSetTime = timeThis { learningCurveREPBoosting(dataSet, ::absoluteError) }
    System.out.println("elapsed time: $originalDataSetTime")


    System.out.println("random projection dataset");
    val randomProjectionDataSetTime = timeThis { learningCurveREPBoosting(bestProjectedDataSet, ::absoluteError) }
    System.out.println("elapsed time: $randomProjectionDataSetTime")
}

fun randomProjectionsWithNNet(dataSet: DataSet<Instance>, errorFunction: (Double) -> Double, nnetParams: Map<String, Any>, numFeaturesOut: Int) {
    val nNet = NeuralNetAlgorithm()

    nNet.setParams(nnetParams)

    val randomProjections = RandomizedProjectionsWrapper(dataSet, nNet, errorFunction)

    val iterations = 100

    val bestProjectedDataSet = randomProjections.findBestRandomProjection(numFeaturesOut, iterations)

    val nNetParams = NeuralNetAlgorithm.createParams(intArrayOf(13), 0.0078f, 500)

    System.out.println("original dataset")
    val originalDataSetTime = timeThis { neuralNetLearningCurve(dataSet, errorFunction, nNetParams) }
    System.out.println("elapsed time: $originalDataSetTime")

    System.out.println("random projection dataset")
    val randomProjectionSetTime = timeThis { neuralNetLearningCurve(bestProjectedDataSet, errorFunction, nNetParams) }
    System.out.println("elapsed time: $randomProjectionSetTime")

    System.out.println(bestProjectedDataSet)
}