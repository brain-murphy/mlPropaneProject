package clusterers

import algorithms.clusterers.KMeansAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.PropaneDataReader
import util.CrossValidation
import util.Csv

fun main(args: Array<String>) {
    crossValidateForK(PropaneDataReader().propaneDataSet as DataSet<Instance>)
}

fun crossValidateForK(dataSet: DataSet<Instance>) {
    val kMeans = KMeansAlgorithm()

    val numFolds = 20

    val csv = Csv("K", "TrainingError", "ValidationError")

    for (k in 2..20) {
        kMeans.setParams(KMeansAlgorithm.createParams(k, KMeansAlgorithm.EUCLIDEAN_DISTANCE_FUNCTION))

        val results = CrossValidation(CrossValidation.AbsoluteError(), numFolds, dataSet, kMeans).run()

        csv.addRow(k, results.meanTrainingError, results.meanValidationError)
    }

}
