@file:JvmName("DecisionTreeExperiments")

import algorithms.DecisionTreeAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.PcaPropaneDataReader
import util.CrossValidation

fun main(args: Array<String>) {
    runDefaultREPTree(PcaPropaneDataReader().propaneDataSet as DataSet<Instance>)
}

fun runDefaultREPTree(dataSet: DataSet<Instance>) {

    val decisionTree = DecisionTreeAlgorithm()

    decisionTree.setParams(DecisionTreeAlgorithm.createParams(false));

    val foldCount = 20

    val crossValidation = CrossValidation(CrossValidation.AbsoluteError(), foldCount, dataSet, decisionTree)

    val result = crossValidation.run()

    println("default REP Tree validation error:${result.meanValidationError}")
}


