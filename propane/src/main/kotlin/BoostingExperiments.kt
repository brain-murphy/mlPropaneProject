@file:JvmName("BoostingExperiments")

import algorithms.classifiers.BoostingClassifier
import algorithms.classifiers.Classifier
import datasets.DataSet
import datasets.Instance
import datasets.PcaPropaneDataReader
import datasets.PropaneDataReader
import analysis.statistical.crossvalidation.SyncCrossValidation
import analysis.statistical.LearningCurve
import analysis.statistical.crossvalidation.AsyncCrossValidator
import analysis.statistical.errorfunction.AvgAbsoluteError
import analysis.statistical.errorfunction.ErrorFunction

fun main(args: Array<String>) {
    val absoluteError =  { difference: Double -> Math.abs(difference)}

    learningCurveREPBoosting(PropaneDataReader().propaneDataSet, absoluteError)
}

fun testDecisionStumpBoosting(dataSet: DataSet<Instance>) {
    val boostingAlgorithm = BoostingClassifier()

    val numFolds = 20

    boostingAlgorithm.setParams(BoostingClassifier.createParams("weka.classifiers.trees.DecisionStump", 50))

    val crossValidation = SyncCrossValidation(SyncCrossValidation.AbsoluteError(), numFolds, dataSet, boostingAlgorithm)

    println(crossValidation.run().meanValidationError)
}

fun testREPBoosting(dataSet: DataSet<Instance>) {
    val boostingAlgorithm = BoostingClassifier()

    val numFolds = 20

    boostingAlgorithm.setParams(BoostingClassifier.createParams("weka.classifiers.trees.REPTree", 50))

    val crossValidation = SyncCrossValidation(SyncCrossValidation.AbsoluteError(), numFolds, dataSet, boostingAlgorithm)

    println(crossValidation.run().meanValidationError)
    var magnitude = 1000000.0
    while (magnitude >= .0000001) {
        magnitude /= 10.0

    }
}

fun learningCurveREPBoosting(dataSet: DataSet<out Instance>, errorFunction: (Double) -> Double) {
    val boostingAlgorithm = BoostingClassifier()

    boostingAlgorithm.setParams(BoostingClassifier.createParams("weka.classifiers.trees.REPTree", 50))

    val learningCurve = LearningCurve(PcaPropaneDataReader().propaneDataSet, boostingAlgorithm, errorFunction, 20)

    val result = learningCurve.run()

    println(result.toString())
}

fun repBoostingError(dataSet: DataSet<Instance>): Double {
    val booster = (BoostingClassifier() as Classifier).javaClass

    val params = BoostingClassifier.BoostingParams("weka.classifiers.trees.DecisionStump", 50)

    val cv = AsyncCrossValidator(dataSet, booster, params, AvgAbsoluteError().asErrorFunction())

    return cv.run().second
}
