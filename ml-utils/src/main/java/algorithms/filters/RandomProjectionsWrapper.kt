package algorithms.filters

import algorithms.Algorithm
import algorithms.classifiers.Classifier
import analysis.statistical.crossvalidation.AsyncCrossValidator
import datasets.DataSet
import datasets.Instance
import org.apache.commons.math3.stat.descriptive.SummaryStatistics
import analysis.statistical.crossvalidation.SyncCrossValidation
import analysis.statistical.errorfunction.ErrorFunction
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread


class RandomProjectionsWrapper(private val dataSet: DataSet<Instance>,
                               private val classifier: Classifier,
                               private val classifierParams: Algorithm.Params,
                               private val errorFunction: ErrorFunction<Number>) : Filter {

    var params: RandomProjectionsWrapperParams? = null

    override fun setParams(params: Algorithm.Params) {
        this.params = params as RandomProjectionsWrapperParams
    }

    override fun filterDataSet(input: DataSet<Instance>?): DataSet<Instance> {
        return findBestRandomProjection(params!!.numProjectedFeatures, params!!.numIterations)
    }

    override fun filterInstance(instance: Instance?): Instance {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setParams(params: MutableMap<String, Any>?) {
        throw UnsupportedOperationException("not implemented")
    }

    private fun findBestRandomProjection(numFeaturesOut: Int, numIterations: Int): DataSet<Instance> {

        var bestValidationError = Double.MAX_VALUE
        var bestDataSet: DataSet<Instance>? = null

        for (i in 1..numIterations) {
            val randomizedProjection = RandomProjectionFilter()

            randomizedProjection.numFeaturesOut = numFeaturesOut

            val projectedData = randomizedProjection.filterDataSet(dataSet.deepCopy())

            val validationError = getValidationError(projectedData)

            if (validationError < bestValidationError) {
                bestDataSet = projectedData
                bestValidationError = validationError
            }
        }

        return bestDataSet!!
    }

    private fun getValidationError(testSet: DataSet<Instance>): Double {
        val results = AsyncCrossValidator(testSet, classifier.javaClass, classifierParams, errorFunction, maxThreads = 3).run()

        return results
    }

    class RandomProjectionsWrapperParams(val numProjectedFeatures: Int,
                                         val numIterations: Int,
                                         val crossValidationFolds: Int = 10) : Algorithm.Params()
}