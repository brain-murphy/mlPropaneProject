package util

import algorithms.classifiers.Classifier
import analysis.Result
import datasets.*
import datasets.Instance
import org.apache.commons.math3.stat.descriptive.*
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis

import java.util.*

object MLUtils {
    fun toPrimitiveFloatArray(floatCollection: Collection<Float>): FloatArray {
        val primitiveArray = FloatArray(floatCollection.size)
        val iterator = floatCollection.iterator()

        for (i in floatCollection.indices) {
            primitiveArray[i] = iterator.next()
        }

        return primitiveArray
    }

    fun crossValidate(dataSet: DataSet<*>, numFolds: Int, classifier: Classifier): Result {
        val groups = dataSet.splitDataSetRandomly(numFolds)

        val validationErrors = SummaryStatistics()
        val trainingErrors = SummaryStatistics()

        for (i in 0..numFolds - 1) {
            val testingData = groups[i].toTypedArray()

            val trainingDataSet = DataSet(combineAllListsExceptOne(groups, i), dataSet.hasDiscreteOutput())

            classifier.train(trainingDataSet)

            var sumOfValidationError = 0.0
            var sumOfTrainingError = 0.0

            for (testInstance in testingData) {
                val output = classifier.evaluate(testInstance.input) as Double
                sumOfValidationError += testInstance.getDifference(output)
            }

            for (trainingInstance in trainingDataSet.getInstances()) {
                val output = classifier.evaluate(trainingInstance.input) as Double
                sumOfTrainingError += trainingInstance.getDifference(output)
            }

            validationErrors.addValue(sumOfValidationError / testingData.size)
            trainingErrors.addValue(sumOfTrainingError / trainingDataSet.getInstances().size)
        }
        return Result(validationErrors.mean, trainingErrors.mean, validationErrors.standardDeviation, numFolds, groups[0].size)
    }

    private fun combineAllListsExceptOne(lists: Array<out List<Instance>>, listToLeaveOut: Int): Array<Instance> {
        if (lists.size == 1) {
            return lists[0].toTypedArray()
        }

        val combinedArray = arrayOfNulls<Instance>(totalCountOfAllLists(lists) - lists[listToLeaveOut].size)

        var combinedArrayIndex = 0

        for (listIndex in lists.indices) {
            if (listIndex == listToLeaveOut) {
                continue
            }

            for (instance in lists[listIndex]) {
                combinedArray[combinedArrayIndex] = instance
                combinedArrayIndex += 1
            }
        }

        return combinedArray.requireNoNulls()
    }

    private fun totalCountOfAllLists(lists: Array<out List<Instance>>): Int {
        var total = 0
        for (list in lists) {
            total += list.size
        }

        return total
    }

    fun leaveOneOutCrossValidate(dataSet: DataSet<*>, classifier: Classifier): Result {
        return crossValidate(dataSet, dataSet.getInstances().size, classifier)
    }

    fun printLearningCurve(dataSet: DataSet<*>, classifier: Classifier) {

        println("percentOfDataForTesting,trainingError,crossValidationError")
        for (divisor in 2..10) {
            val results = crossValidate(dataSet, divisor, classifier)

            println(String.format("%.3f", 1.0f / divisor) + "," + results.meanTrainingError + "," + String.format("%.3f", results.meanValidationError))
        }

        val results = leaveOneOutCrossValidate(dataSet, classifier)

        println(String.format("%.3f", 1.0f / dataSet.getInstances().size) + "," + results.meanTrainingError + "," + String.format("%.3f", results.meanValidationError))
    }

    fun getNumInputs(dataSet: DataSet<Instance>): Int {
        val firstInstance = dataSet.getInstances()[0]

        return firstInstance.input.size
    }

    fun createCsvForClusterResults(instances: Array<Instance>, clusters: IntArray): Csv {
        val csv = Csv("InstanceIndex", "Output", "Classification")

        for (i in instances.indices) {
            csv.addRow(i.toInt(), instances[i].output, clusters[i])
        }

        return csv
    }

    fun calculateAverageKurtosisForAttributes(dataSet: DataSet<Instance>): Double {
        val kurtosis = Kurtosis()

        val instances = dataSet.getInstances()

        val numDims = instances[0].input.size

        val columns = Array(numDims) { DoubleArray(instances.size) }

        for (dimensionIndex in 0..numDims - 1) {
            for (instanceIndex in instances.indices) {
                columns[dimensionIndex][instanceIndex] = instances[instanceIndex].input[dimensionIndex]
            }
        }

        val stats = SummaryStatistics()
        for (col in columns) {
            stats.addValue(kurtosis.evaluate(col))
        }

        return stats.mean
    }

    @JvmStatic fun main(args: Array<String>) {
        println(PropaneDataReader().get2013PropaneDataSet().toString())
    }
}


fun absoluteError(difference: Double): Double {
    return Math.abs(difference)
}
