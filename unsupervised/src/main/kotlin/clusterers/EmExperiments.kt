package clusterers

import algorithms.clusterers.EmClusterer
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import datasets.PropaneDataReader
import util.MLUtils

fun main(args: Array<String>) {
    emIrisDataStepOne()
}

fun emIrisDataStepOne() {
    val clusterCount = 3

    val dataSet = IrisDataReader().irisDataSet

    val clusters = clusterWithEm(dataSet, clusterCount)

    val clusterCsv = MLUtils.createCsvForClusterResults(dataSet.getInstances(), clusters)

    System.out.println(clusterCsv)
}
fun emPropaneDataStepOne() {
    val clusterCount = 2

    val dataSet = PropaneDataReader().propaneDataSet

    val clusters = clusterWithEm(dataSet, clusterCount)

    val clusterCsv = MLUtils.createCsvForClusterResults(dataSet.getInstances(), clusters)

    System.out.println(clusterCsv)
}

fun clusterWithEm(dataSet: DataSet<Instance>, clusterCount: Int): IntArray {
    val clusterer = EmClusterer()

    clusterer.setParams(EmClusterer.createParams(clusterCount))

    clusterer.train(dataSet)

    return clusterer.clusters
}
