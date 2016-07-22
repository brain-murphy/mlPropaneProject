package algorithms.filters

import algorithms.classifiers.Classifier
import datasets.DataSet
import datasets.Instance
import org.apache.commons.math3.stat.descriptive.SummaryStatistics
import analysis.statistical.CrossValidation
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread


class RandomizedProjectionsWrapper(private val dataSet: DataSet<Instance>,
                                   private val classifier: Classifier,
                                   private val errorFunction: (Double) -> Double) : Filter {

    override fun filterDataSet(input: DataSet<Instance>?): DataSet<Instance> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun filterInstance(instance: Instance?): Instance {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setParams(params: MutableMap<String, Any>?) {
        throw UnsupportedOperationException("not implemented")
    }

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
        val results = CrossValidation(errorFunction, crossValidationFolds, testSet, classifier).run()

        return results.meanValidationError
    }
}