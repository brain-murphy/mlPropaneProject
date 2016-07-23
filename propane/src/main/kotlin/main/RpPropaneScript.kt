package main

import algorithms.classifiers.KNearestNeighborsClassifier
import algorithms.filters.RandomProjectionsWrapper
import analysis.statistical.errorfunction.AvgAbsoluteError
import analysis.statistical.errorfunction.ErrorFunction
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import datasets.PropaneDataReader
import util.GeneralUtils


fun main(args: Array<String>) {
    asyncRpUsingKnn(PropaneDataReader().propaneDataSet, 3, 90000)
}

fun asyncRpUsingKnn(dataSet: DataSet<Instance>, numProjectedFeatures: Int, iterations: Int) {
    val classifier = KNearestNeighborsClassifier()

    val twoNearestNeighbors = 2

    val params = KNearestNeighborsClassifier.KnnParams(twoNearestNeighbors)

    val errorFunction = AvgAbsoluteError() as ErrorFunction<Number>

    val rp = RandomProjectionsWrapper(dataSet, classifier, params, errorFunction)

    rp.params = RandomProjectionsWrapper.RandomProjectionsWrapperParams(numProjectedFeatures, iterations)

    val resultDataSet = rp.filterDataSet(dataSet)

    println(resultDataSet)

    GeneralUtils.writeToFile("Rp5dPropaneData.csv", resultDataSet.toString())
}