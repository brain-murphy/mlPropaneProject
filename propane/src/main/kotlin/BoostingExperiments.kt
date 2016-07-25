@file:JvmName("BoostingExperiments")

import algorithms.classifiers.BoostingClassifier
import algorithms.classifiers.Classifier
import analysis.statistical.AsyncLearningCurve
import datasets.DataSet
import datasets.Instance
import datasets.PropaneDataReader
import analysis.statistical.crossvalidation.SyncCrossValidation
import analysis.statistical.crossvalidation.AsyncCrossValidator
import analysis.statistical.errorfunction.AvgAbsoluteError

fun main(args: Array<String>) {
    learningCurveREPBoosting(PropaneDataReader().propaneDataSet)
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

fun learningCurveREPBoosting(dataSet: DataSet<out Instance>) {
    val boostingAlgorithm = (BoostingClassifier() as Classifier).javaClass

    val params = BoostingClassifier.BoostingParams("weka.classifiers.trees.REPTree", 50)

    val learningCurve = AsyncLearningCurve(dataSet, boostingAlgorithm, params, AvgAbsoluteError().asErrorFunction())

    val result = learningCurve.run()

    println(result.toString())
}

fun repBoostingError(dataSet: DataSet<Instance>): Double {
    val booster = (BoostingClassifier() as Classifier).javaClass

    val params = BoostingClassifier.BoostingParams("weka.classifiers.trees.REPTree", 50)

    val cv = AsyncCrossValidator(dataSet, booster, params, AvgAbsoluteError().asErrorFunction())

    return cv.run().second
}

fun decisionStumpBoostingError(dataSet: DataSet<Instance>): Double {
    val booster = (BoostingClassifier() as Classifier).javaClass

    val params = BoostingClassifier.BoostingParams("weka.classifiers.trees.DecisionStump", 50)

    val cv = AsyncCrossValidator(dataSet, booster, params, AvgAbsoluteError().asErrorFunction())

    return cv.run().second
}
