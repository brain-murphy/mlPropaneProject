package main

import algorithms.classifiers.KNearestNeighborsClassifier
import algorithms.classifiers.SvmClassifier
import algorithms.filters.RandomProjectionsWrapper
import analysis.statistical.errorfunction.AvgAbsoluteError
import analysis.statistical.errorfunction.ErrorFunction
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import datasets.PropaneDataReader
import util.GeneralUtils


fun main(args: Array<String>) {
    val result2016 = asyncRpUsingSvm(PropaneDataReader().get2016PropaneDataSet(), 3, 6000)

    println(result2016)
    GeneralUtils.writeToFile("Rp3dPropaneData2016_Svm.csv", result2016.toString())

    val result2013 = asyncRpUsingSvm(PropaneDataReader().get2013PropaneDataSet(), 3, 6000)

    println(result2013)
    GeneralUtils.writeToFile("Rp3dPropaneData2013_Svm.csv", result2013.toString())
}

fun asyncRpUsingKnn(dataSet: DataSet<Instance>, numProjectedFeatures: Int, iterations: Int): DataSet<Instance> {
    val classifier = KNearestNeighborsClassifier()
    val twoNearestNeighbors = 2
    val params = KNearestNeighborsClassifier.KnnParams(twoNearestNeighbors)
    val errorFunction = AvgAbsoluteError() as ErrorFunction<Number>

    val rp = RandomProjectionsWrapper(classifier, params, errorFunction)
    rp.params = RandomProjectionsWrapper.RandomProjectionsWrapperParams(numProjectedFeatures, iterations)

   return rp.filterDataSet(dataSet)
}

fun asyncRpUsingSvm(dataSet: DataSet<Instance>, numProjectedFeatures: Int, iterations: Int): DataSet<Instance> {
    val classifier = SvmClassifier()
    val params = SvmClassifier.SvmParams(SvmClassifier.Kernel.Linear, 1.0, 1.0)
    val errorFunction = AvgAbsoluteError().asErrorFunction()

    val rp = RandomProjectionsWrapper(classifier, params, errorFunction)
    rp.params = RandomProjectionsWrapper.RandomProjectionsWrapperParams(numProjectedFeatures, iterations)

    return rp.filterDataSet(dataSet)
}