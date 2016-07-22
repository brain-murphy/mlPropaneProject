@file:JvmName("DecisionTreeExperiments")

import algorithms.classifiers.DecisionTreeClassifier
import datasets.DataSet
import datasets.Instance
import datasets.PropaneDataReader
import analysis.statistical.SyncCrossValidation
import analysis.statistical.LearningCurve

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

fun runDefaultREPTree(dataSet: DataSet<Instance>) {

    val decisionTree = DecisionTreeClassifier()

    decisionTree.setParams(DecisionTreeClassifier.createParams(DecisionTreeClassifier.Type.RepTree))

    val foldCount = 20

    val crossValidation = SyncCrossValidation(CrossValidation.AbsoluteError(), foldCount, dataSet, decisionTree)

    val result = crossValidation.run()

    println("default REP Tree validation error:${result.meanValidationError}")
}


