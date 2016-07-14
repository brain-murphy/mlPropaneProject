package clusterers

import algorithms.clusterers.KMeansAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import util.Csv

fun main(args: Array<String>) {
    System.out.println(threeMeansCluster(IrisDataReader().irisDataSet as DataSet<Instance>))
}

fun twoMeansCluster(dataSet: DataSet<Instance>): Csv {
    val k = 2
    val distanceFunction = KMeansAlgorithm.DistanceFunction.Euclidian

    return runKMeans(dataSet, distanceFunction, k)
}

fun threeMeansCluster(dataSet: DataSet<Instance>): Csv {
    val k = 3
    val distanceFunction = KMeansAlgorithm.DistanceFunction.Euclidian

    return runKMeans(dataSet, distanceFunction, k)
}

private fun runKMeans(dataSet: DataSet<Instance>, distanceFunction: KMeansAlgorithm.DistanceFunction, k: Int): Csv {
    val kMeans = KMeansAlgorithm()

    kMeans.setParams(KMeansAlgorithm.createParams(k, distanceFunction))

    kMeans.train(dataSet);

    val classifications = kMeans.evaluate(null) as IntArray
    val instances = dataSet.getInstances()

    return createCsvForClusterResults(instances, classifications)
}

private fun createCsvForClusterResults(instances: Array<Instance>, classifications: IntArray): Csv {
    val csv = Csv("InstanceIndex", "Output", "Classification")

    for (i in 0..classifications.size - 1) {
        csv.addRow(i, instances[i].output, classifications[i])
    }

    return csv
}

