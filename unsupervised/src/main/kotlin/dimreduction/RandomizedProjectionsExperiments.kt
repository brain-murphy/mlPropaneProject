package dimreduction

import algorithms.BoostingAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.PropaneDataReader
import filters.RandomizedProjectionsWrapper
import learningCurveREPBoosting
import util.ElapsedTime
import util.MLUtils
import util.absoluteError


fun main(args: Array<String>) {
    randomProjectionsWithBoosting(PropaneDataReader().propaneDataSet, ::absoluteError)
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