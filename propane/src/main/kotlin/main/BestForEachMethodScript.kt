package main

import datasets.DataSet
import datasets.Instance
import datasets.PropaneDataReader
import repBoostingError
import repTreeError
import knnError
import nNetError
import rbfError
import svmError
import util.Csv


fun main(args: Array<String>) {
    println(testAllDataSetsOnAllMethods())
}

val fullDataSet = PropaneDataReader().propaneDataSet

fun printAllMethods() {
    println("best boosting error:\t\t ${getBestBoostingError()}")
    println("best decision tree error:\t\t ${getBestDecisionTreeError()}")
    println("best knn error:\t\t ${getBestKnnError()}")
    println("best nNet error:\t\t ${getBestNNetError()}")
    println("best rbf error:\t\t ${getBestRbfError()}")
    println("best svm error:\t\t ${getBestSvmError()}")
}

fun testAllDataSetsOnAllMethods(): Csv {
    val csv = Csv("Boosting", "DecisionTree", "Knn", "NeuralNet", "RBF", "SVM")

    for (dataSet in PropaneDataReader().allDataSets) {
        println("starting row")
        csv.addRow(*testAllMethods(dataSet))
    }

    return csv
}

fun testAllMethods(dataSet: DataSet<Instance>): Array<Double> {
    return arrayOf(getBestBoostingError(),
            getBestDecisionTreeError(),
            getBestKnnError(),
            getBestNNetError(),
            getBestRbfError(),
            getBestSvmError())
}

fun getBestBoostingError(): Double {
    return repBoostingError(fullDataSet)
}

fun getBestDecisionTreeError(): Double {
    return repTreeError(fullDataSet)
}

fun getBestKnnError(): Double {
    return knnError(fullDataSet)
}

fun getBestNNetError(): Double {
    return nNetError(fullDataSet)
}

fun getBestRbfError(): Double {
    return rbfError(fullDataSet)
}

fun getBestSvmError(): Double {
    return svmError(fullDataSet)
}