package analysis.statistical

import algorithms.Algorithm
import algorithms.classifiers.Classifier
import analysis.statistical.crossvalidation.AsyncCrossValidator
import analysis.statistical.crossvalidation.SyncCrossValidation
import analysis.statistical.errorfunction.ErrorFunction
import datasets.DataSet
import datasets.Instance
import util.Csv
import java.util.*


class AsyncLearningCurve(private val dataSet: DataSet<out Instance>,
                         private val classifier: Class<Classifier>,
                         private val params: Algorithm.Params,
                         private val errorFunction: ErrorFunction<Number>,
                         private val foldCount: Int = 10) {


    private val allButOne:Double = 0.99999999999
    private val CSV_HEADER = arrayOf("ProportionOfData", "TrainingError", "ValidationError")

    private var leaveOneOutCrossValidation = false;


    fun run(): Csv {
        val csvResults = Csv(*CSV_HEADER)

        for (iteration in 1..50) {
            val proportion = getProportionOfDataToUse(iteration)

            val dataToUse = splitDataSetProportionately(proportion, 1.0 - proportion)[0]

            if (leaveOneOutCrossValidation) {
                val results = AsyncCrossValidator(dataToUse, classifier, params, errorFunction, dataSet.getInstances().size).run()

                csvResults.addRow(proportion, results.first, results.second)

            } else if (dataToUse.getInstances().size >= foldCount) {
                val results = AsyncCrossValidator(dataToUse, classifier, params, errorFunction, foldCount).run()

                csvResults.addRow(proportion, results.first, results.second)
            }
        }

        return csvResults
    }

    fun enableLeaveOneOutCV() {
        leaveOneOutCrossValidation = true
    }

    private fun getProportionOfDataToUse(iteration: Int): Double {
        return iteration.toDouble() / 50.0
    }

    private fun splitDataSetProportionately(vararg proportions: Double) : Array<DataSet<Instance>> {
        if (proportions.reduce { total, current -> total + current } < allButOne) {
            throw IllegalArgumentException("proportions must sum to one")
        }

        val dataSetSize = dataSet.getInstances().size

        val sizesOfSplits = proportions.map { proportion -> Math.floor(proportion * dataSetSize).toInt() }

        val random = Random()

        val allInstances = mutableListOf(*dataSet.getInstances())
        val resultingDataSets = LinkedList<DataSet<Instance>>()

        for (splitSize in sizesOfSplits) {
            val split = LinkedList<Instance>()

            for (index in 1..splitSize) {
                split.add(allInstances.removeAt(random.nextInt(allInstances.size)))
            }

            resultingDataSets.add(DataSet(split.toTypedArray(), dataSet.hasDiscreteOutput()))
        }

        return resultingDataSets.toTypedArray()
    }
}