@file:JvmName("NeuralNetExperiments")

import algorithms.NeuralNetAlgorithm
import datasets.DataSet
import datasets.Instance
import datasets.PcaPropaneDataReader
import datasets.PropaneDataReader
import util.CrossValidation
import util.CsvPrinter

fun findHiddenLayerLength(dataSet: DataSet<Instance>) {

    val csv = CsvPrinter(arrayOf("HiddenLayerSize", "TrainingError", "ValidationError"))

    for (hiddenLayerLength in 2..149) {
        val neuralNet = NeuralNetAlgorithm()

        neuralNet.setParams(NeuralNetAlgorithm.createParams(intArrayOf(hiddenLayerLength), .001f, 1000))

        val crossValidation = CrossValidation(CrossValidation.AbsoluteError(), 10, dataSet, neuralNet)

        val result = crossValidation.run()

        csv.addRow(hiddenLayerLength.toDouble(), result.meanTrainingError, result.meanValidationError)
    }
}

fun findTrainingErrorThreshold(dataSet: DataSet<Instance>) {

    println("TrainingErrorThreshold,TrainingError,ValidationError")

    var divisor = 2
    while (divisor < 1025) {
        val neuralNet = NeuralNetAlgorithm()

        val trainingErrorThreshold = 1.0f / divisor

        neuralNet.setParams(NeuralNetAlgorithm.createParams(intArrayOf(9, 8), trainingErrorThreshold, 1000))

        val crossValidation = CrossValidation(CrossValidation.AbsoluteError(), 10, dataSet, neuralNet)

        val result = crossValidation.run()

        println("$trainingErrorThreshold,${result.meanTrainingError},${result.meanValidationError}")
        divisor = divisor shl 1
    }
}

fun main(args: Array<String>) {
    moonshot()
}

fun moonshot() {
    val neuralNet = NeuralNetAlgorithm()

    neuralNet.setParams(NeuralNetAlgorithm.createParams(intArrayOf(9, 8), 0.006f, 500))

    val dataSet = PcaPropaneDataReader().propaneDataSet as DataSet<Instance>

    val crossValidation = CrossValidation(CrossValidation.AbsoluteError(), 60, dataSet, neuralNet)

    val result = crossValidation.run()

    println("validation Error:" + result.getMeanValidationError())
}

fun moonshot2() {

    val neuralNet = NeuralNetAlgorithm()

    neuralNet.setParams(NeuralNetAlgorithm.createParams(intArrayOf(100), .005f, 10000))

    val dataSet = PropaneDataReader().propaneDataSet as DataSet<Instance>

    val crossValidation = CrossValidation(CrossValidation.AbsoluteError(), 10, dataSet, neuralNet)

    val result = crossValidation.run()

    println("validation Error:" + result.getMeanValidationError())
}

fun gridSearchTwoLayerStructure(dataSet: DataSet<Instance>, maxFirstLayer: Int, maxSecondLayer: Int) {

    println("FirstHiddenLayerLength,SecondHiddenLayerLength,TrainingError,ValidationError")

    for (firstLayerLength in 2..maxFirstLayer - 1) {

        var secondLayerLength = 2
        while (secondLayerLength < firstLayerLength && secondLayerLength < maxSecondLayer) {
            val neuralNet = NeuralNetAlgorithm()

            neuralNet.setParams(NeuralNetAlgorithm.createParams(intArrayOf(firstLayerLength, secondLayerLength), .006f, 10000))

            val crossValidation = CrossValidation(CrossValidation.AbsoluteError(), 10, dataSet, neuralNet)

            val result = crossValidation.run()

            println("$firstLayerLength,$secondLayerLength,${result.meanTrainingError},${result.meanValidationError}")
            secondLayerLength++

        }
    }
}
