package main

import datasets.DataSet
import datasets.Instance
import datasets.PropaneDataReader
import repBoostingError
import repTreeError
import knnError
import nNetError
import main.rbfError
import svmError
import util.Csv
import util.GeneralUtils


fun main(args: Array<String>) {
    val csv = testAllDataSetsOnAllMethods()
    println(csv.toString())
    GeneralUtils.writeToFile("methodsVsDataSetComparison.csv", csv.toString())
}

val fullDataSet = PropaneDataReader().propaneDataSet

fun printAllMethods() {
    println("best boosting error:\t\t ${getBestBoostingError(fullDataSet)}")
    println("best decision tree error:\t\t ${getBestDecisionTreeError(fullDataSet)}")
    println("best knn error:\t\t ${getBestKnnError(fullDataSet)}")
    println("best nNet error:\t\t ${getBestNNetError(fullDataSet)}")
    println("best rbf error:\t\t ${getBestRbfError(fullDataSet)}")
    println("best svm error:\t\t ${getBestSvmError(fullDataSet)}")
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
    return arrayOf(getBestBoostingError(dataSet),
            getBestDecisionTreeError(dataSet),
            getBestKnnError(dataSet),
            getBestNNetError(dataSet),
            getBestRbfError(dataSet),
            getBestSvmError(dataSet))
}

fun getBestBoostingError(dataSet: DataSet<Instance>): Double {
    return repBoostingError(dataSet)
}

fun getBestDecisionTreeError(dataSet: DataSet<Instance>): Double {
    return repTreeError(dataSet)
}

fun getBestKnnError(dataSet: DataSet<Instance>): Double {
    return knnError(dataSet)
}

fun getBestNNetError(dataSet: DataSet<Instance>): Double {
    return nNetError(dataSet)
}

fun getBestRbfError(dataSet: DataSet<Instance>): Double {
    return rbfError(dataSet)
}

fun getBestSvmError(dataSet: DataSet<Instance>): Double {
    return svmError(dataSet)
}