package clusterers

import algorithms.clusterers.KMeansAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import datasets.PropaneDataReader
import util.Csv
import util.MLUtils

fun main(args: Array<String>) {
    kMeansIrisDataStepOne()
}

fun kMeansIrisDataStepOne() {
    val dataSet = IrisDataReader().irisDataSet

    val distanceFunction = KMeansAlgorithm.DistanceFunction.Euclidean

    val k = 3

    val clusters = clusterWithKMeans(dataSet, distanceFunction, k)

    val clusterCsv = MLUtils.createCsvForClusterResults(dataSet.getInstances(), clusters)

    System.out.println(clusterCsv)
}
fun kMeansPropaneDataStepOne() {
    val dataSet = PropaneDataReader().propaneDataSet

    val distanceFunction = KMeansAlgorithm.DistanceFunction.MostProminentAttributeDistanceFunction

    val k = 2

    val clusters = clusterWithKMeans(dataSet, distanceFunction, k)

    val clusterCsv = MLUtils.createCsvForClusterResults(dataSet.getInstances(), clusters)

    System.out.println(clusterCsv)
}

fun clusterWithKMeans(dataSet: DataSet<Instance>, distanceFunction: KMeansAlgorithm.DistanceFunction, k: Int): IntArray {
    val kMeans = KMeansAlgorithm()

    kMeans.setParams(KMeansAlgorithm.createParams(k, distanceFunction))

    kMeans.train(dataSet)

    val clusterings = kMeans.clusters

    return clusterings
}

