package analysis

import algorithms.classifiers.Classifier
import algorithms.clusterers.Clusterer
import algorithms.filters.Filter
import datasets.DataSet
import datasets.Instance

class Experiment(val dataSet: DataSet<Instance>,
                 val classifier: Classifier? = null,
                 val clusterer: Clusterer? = null) {

    val filters = mutableListOf<Filter>()

    fun run() {

        val currentDataSet = filterDataSet()

        evaluateClassifier(currentDataSet)

        evaluateClusterer(currentDataSet)
    }

    private fun evaluateClusterer(currentDataSet: DataSet<Instance>) {
        clusterer?.train(currentDataSet)
    }

    private fun evaluateClassifier(currentDataSet: DataSet<Instance>) {
        classifier?.train(currentDataSet)
    }

    private fun filterDataSet(): DataSet<Instance> {
        var currentDataSet = dataSet

        filters.forEach { currentDataSet = it.filterDataSet(currentDataSet) }

        return currentDataSet
    }

}


