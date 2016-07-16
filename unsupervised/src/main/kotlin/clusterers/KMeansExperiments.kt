package clusterers

import algorithms.clusterers.KMeansAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import util.Csv
import util.MLUtils

fun main(args: Array<String>) {
    val distanceFunction = KMeansAlgorithm.DistanceFunction.Euclidean

    val k = 3

    System.out.println(runKMeans(IrisDataReader().csfDataSet, distanceFunction, k))
}

fun sixMeansCluster(dataSet: DataSet<Instance>, distanceFunction: KMeansAlgorithm.DistanceFunction): Csv {
    val k = 2

    return runKMeans(dataSet, distanceFunction, k)
}

fun threeMeansCluster(dataSet: DataSet<Instance>, distanceFunction: KMeansAlgorithm.DistanceFunction): Csv {
    val k = 3

    return runKMeans(dataSet, distanceFunction, k)
}

private fun runKMeans(dataSet: DataSet<Instance>, distanceFunction: KMeansAlgorithm.DistanceFunction, k: Int): Csv {
    val kMeans = KMeansAlgorithm()

    kMeans.setParams(KMeansAlgorithm.createParams(k, distanceFunction))

    kMeans.train(dataSet)

    val clusterings = kMeans.clusters

    return MLUtils.createCsvForClusterResults(clusterings)
}

