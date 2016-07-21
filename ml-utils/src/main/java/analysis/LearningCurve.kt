package analysis

import algorithms.classifiers.Classifier
import datasets.DataSet
import datasets.Instance
import org.apache.commons.math3.stat.descriptive.SummaryStatistics
import util.Csv
import java.util.*



class LearningCurve(private val dataSet: DataSet<out Instance>,
                    private val classifier: Classifier,
                    private val errorFunction: (Double) -> Double,
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
                val results = CrossValidation(errorFunction, dataToUse.getInstances().size, dataToUse, classifier).run()

                csvResults.addRow(proportion, results.meanTrainingError, results.meanValidationError)

            } else if (dataToUse.getInstances().size >= foldCount) {
                val results = CrossValidation(errorFunction, foldCount, dataToUse, classifier).run()

                csvResults.addRow(proportion, results.meanTrainingError, results.meanValidationError)
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