@file:JvmName("BoostingExperiments")

import algorithms.BoostingAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.PcaPropaneDataReader
import datasets.PropaneDataReader
import util.CrossValidation
import util.LearningCurve

fun main(args: Array<String>) {
    val absoluteError =  { difference: Double -> Math.abs(difference)}

    learningCurveREPBoosting(PropaneDataReader().propaneDataSet, absoluteError)
}

fun testDecisionStumpBoosting(dataSet: DataSet<Instance>) {
    val boostingAlgorithm = BoostingAlgorithm()

    val numFolds = 20

    boostingAlgorithm.setParams(BoostingAlgorithm.createParams("weka.classifiers.trees.DecisionStump", 50))

    val crossValidation = CrossValidation(CrossValidation.AbsoluteError(), numFolds, dataSet, boostingAlgorithm)

    println(crossValidation.run().meanValidationError)
}

fun testREPBoosting(dataSet: DataSet<Instance>) {
    val boostingAlgorithm = BoostingAlgorithm()

    val numFolds = 20

    boostingAlgorithm.setParams(BoostingAlgorithm.createParams("weka.classifiers.trees.REPTree", 50))

    val crossValidation = CrossValidation(CrossValidation.AbsoluteError(), numFolds, dataSet, boostingAlgorithm)

    println(crossValidation.run().meanValidationError)
    var magnitude = 1000000.0
    while (magnitude >= .0000001) {
        magnitude /= 10.0

    }
}

fun learningCurveREPBoosting(dataSet: DataSet<out Instance>, errorFunction: (Double) -> Double) {
    val boostingAlgorithm = BoostingAlgorithm()

    boostingAlgorithm.setParams(BoostingAlgorithm.createParams("weka.classifiers.trees.REPTree", 50))

    val learningCurve = LearningCurve(PcaPropaneDataReader().propaneDataSet, boostingAlgorithm, errorFunction, 20)

    val result = learningCurve.run()

    println(result.toString())
}
