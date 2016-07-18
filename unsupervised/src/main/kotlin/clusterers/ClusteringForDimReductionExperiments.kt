package clusterers

import algorithms.NeuralNetAlgorithm
import algorithms.clusterers.KMeansAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import neuralNetLearningCurve
import util.Csv
import util.absoluteError
import java.util.*
import javax.xml.crypto.Data

fun main(args: Array<String>) {
    testClusterDataDouble(IrisDataReader().clusteredDataSet)
//    val numClusters = 3
//    System.out.println(dimReductionUsingClusters(IrisDataReader().reducedDataSets, numClusters,KMeansAlgorithm.DistanceFunction.Euclidean))
}

fun dimReductionUsingClusters(dataSets: Array<DataSet<Instance>>, numClusters: Int, kMeansDistance: KMeansAlgorithm.DistanceFunction): Csv {

    val clustererOutputs = LinkedList<IntArray>()

    for (dataSet in dataSets) {
        clustererOutputs.add(clusterWithEm(dataSet, numClusters))
        clustererOutputs.add(clusterWithKMeans(dataSet, kMeansDistance, numClusters))
    }

    val csv = Csv("PcaEm", "PcaKM", "IcaEm", "IcaKM", "RpEm", "RpKM", "CsfEm", "CsfKM", "Output")

    val instances = dataSets[0].getInstances()
    val instanceCount = instances.size

    for (i in 0..instanceCount - 1) {
        csv.addRow(*makeClusterRow(clustererOutputs, i), instances[i].output)
    }

    return csv
}

fun makeClusterRow(clustererOutputs: List<IntArray>, instanceIndex: Int): Array<Int> {
    val rowOutput = LinkedList<Int>()
    for (clustererOutput in clustererOutputs) {
        rowOutput.add(clustererOutput[instanceIndex])
    }

    return rowOutput.toArray(Array(rowOutput.size, { i -> 0 }))
}

fun testClusterData(dataSet: DataSet<Instance>) {
    val nNetParams = NeuralNetAlgorithm.createParams(intArrayOf(13), 0.006f, 500)

    neuralNetLearningCurve(dataSet, ::absoluteError, nNetParams)
}

fun testClusterDataDouble(dataSet: DataSet<Instance>) {
    val instances = dataSet.getInstances()
    val numUniqueInstances = instances.size
    val doubleData = Array<Instance>(numUniqueInstances * 2, { i -> instances[i % numUniqueInstances].deepCopy()})

    testClusterData(DataSet<Instance>(doubleData, dataSet.hasDiscreteOutput()))
}