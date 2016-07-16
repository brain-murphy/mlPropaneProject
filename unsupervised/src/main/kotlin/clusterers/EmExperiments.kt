package clusterers

import algorithms.clusterers.EmClusterer
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import datasets.PropaneDataReader
import util.Csv
import util.MLUtils

fun main(args: Array<String>) {
    val clusterCount = 3

    val clusterCsv = clusterWithEm(IrisDataReader().csfDataSet, clusterCount)

    System.out.println(clusterCsv)
}

fun clusterWithEm(dataSet: DataSet<Instance>, clusterCount: Int): Csv {
    val clusterer = EmClusterer()

    clusterer.setParams(EmClusterer.createParams(clusterCount))

    clusterer.train(dataSet)

    return MLUtils.createCsvForClusterResults(clusterer.clusters)
}