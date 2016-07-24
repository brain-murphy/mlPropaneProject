@file:JvmName("DecisionTreeExperiments")

import algorithms.classifiers.Classifier
import algorithms.classifiers.DecisionTreeClassifier
import datasets.DataSet
import datasets.Instance
import datasets.PropaneDataReader
import analysis.statistical.crossvalidation.SyncCrossValidation
import analysis.statistical.LearningCurve
import analysis.statistical.crossvalidation.AsyncCrossValidator
import analysis.statistical.errorfunction.AvgAbsoluteError

fun main(args: Array<String>) {
    repTreeLearningCurve(PropaneDataReader().propaneDataSet as DataSet<Instance>)
}

fun repTreeLearningCurve(dataSet: DataSet<out Instance>) {
    val decisionTree = DecisionTreeClassifier()

    decisionTree.setParams(DecisionTreeClassifier.createParams(DecisionTreeClassifier.Type.RepTree))

    val absoluteError = { error:Double -> Math.abs(error) }

    val learningCurve = LearningCurve(dataSet, decisionTree, absoluteError)

    val csv = learningCurve.run()

    println(csv.toString())
}

fun repTreeError(dataSet: DataSet<Instance>): Double {

    val decisionTree = (DecisionTreeClassifier() as Classifier).javaClass

    val params = DecisionTreeClassifier.DecisionTreeParams(DecisionTreeClassifier.Type.RepTree)

    val crossValidation = AsyncCrossValidator(dataSet, decisionTree, params, AvgAbsoluteError().asErrorFunction())

    return crossValidation.run().second
}


