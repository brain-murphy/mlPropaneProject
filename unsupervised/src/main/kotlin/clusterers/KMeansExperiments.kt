package clusterers

import algorithms.clusterers.KMeansAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.PropaneDataReader
import util.CrossValidation
import util.Csv

fun main(args: Array<String>) {

}

fun twoMeansCluster(dataSet: DataSet<Instance>): Csv {
    val kMeans = KMeansAlgorithm()

    val k = 2
    val distanceFunction = KMeansAlgorithm.DistanceFunction.Euclidian

    kMeans.setParams(KMeansAlgorithm.createParams(k, distanceFunction))

    kMeans.train(dataSet);

    val classifications = kMeans.evaluate(null) as IntArray

    kMeans.setParams(kMeans)
}


