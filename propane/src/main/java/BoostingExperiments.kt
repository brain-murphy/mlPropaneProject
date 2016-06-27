@file:JvmName("BoostingExperiments")

import algorithms.BoostingAlgorithm
import datasets.DataSet
import datasets.Instance
import util.CrossValidation

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
