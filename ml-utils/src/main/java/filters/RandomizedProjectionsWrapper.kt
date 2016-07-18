package filters

import algorithms.Algorithm
import datasets.DataSet
import datasets.Instance
import org.apache.commons.math3.stat.descriptive.SummaryStatistics
import util.CrossValidation
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread


class RandomizedProjectionsWrapper(private val dataSet: DataSet<Instance>,
                                   private val algorithm: Algorithm,
                                   private val errorFunction: (Double) -> Double) {

    var crossValidationFolds = 10

    fun findBestRandomProjection(numFeaturesOut: Int, numIterations: Int): DataSet<Instance> {

        var bestValidationError = Double.MAX_VALUE
        var bestDataSet: DataSet<Instance>? = null

        val stats = SummaryStatistics()

        for (i in 1..numIterations) {
            val randomizedProjection = RandomizedProjectionFilter()

            randomizedProjection.numFeaturesOut = numFeaturesOut

            val projectedData = randomizedProjection.filterDataSet(dataSet.deepCopy())

            val validationError = getValidationError(projectedData)

            if (validationError < bestValidationError) {
                bestDataSet = projectedData
                bestValidationError = validationError
            }

            stats.addValue(validationError)
        }

        println("errror stdev:${stats.standardDeviation}")

        return bestDataSet!!
    }

    private fun getValidationError(testSet: DataSet<Instance>): Double {
        val results = CrossValidation(errorFunction, crossValidationFolds, testSet, algorithm).run()

        return results.meanValidationError
    }
}