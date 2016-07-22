package analysis.infrastructure

import algorithms.classifiers.Classifier
import algorithms.clusterers.Clusterer
import algorithms.filters.Filter
import datasets.DataSet
import datasets.Instance

open class Experiment(val classifier: Classifier? = null,
                      val clusterer: Clusterer? = null,
                      vararg val filters: Filter) {


    open fun run(dataSet: DataSet<Instance>): Result {

        val currentDataSet = filterDataSet(dataSet)

        evaluateClassifier(currentDataSet)

        evaluateClusterer(currentDataSet)

        return Result(currentDataSet, clusterer, classifier)
    }

    private fun evaluateClusterer(currentDataSet: DataSet<Instance>) {
        clusterer?.train(currentDataSet)
    }

    private fun evaluateClassifier(currentDataSet: DataSet<Instance>) {
        classifier?.train(currentDataSet)
    }

    private fun filterDataSet(dataSet: DataSet<Instance>): DataSet<Instance> {
        var currentDataSet = dataSet

        filters.forEach { currentDataSet = it.filterDataSet(currentDataSet) }

        return currentDataSet
    }

    data class Result(val resultDataSet: DataSet<Instance>,
                      val clusterer: Clusterer? = null,
                      val classifier: Classifier? = null)

}


