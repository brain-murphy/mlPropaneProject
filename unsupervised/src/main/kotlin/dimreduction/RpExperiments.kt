package dimreduction

import algorithms.classifiers.BoostingClassifier
import algorithms.classifiers.NeuralNetClassifier
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import datasets.PropaneDataReader
import algorithms.filters.RandomProjectionsWrapper
import learningCurveREPBoosting
import neuralNetLearningCurve
import util.MLUtils
import util.absoluteError
import util.GeneralUtils.timeThis


fun main(args: Array<String>) {
    rpPropaneStepTwo()
}

fun rpPropaneStepTwo() {
    val nnetParams: Map<String, Any> = NeuralNetClassifier.createParams(intArrayOf(9,8), 0.0078f, 500)


    val propaneDataReader = PropaneDataReader()

    propaneDataReader.setDiscreteMode(true)
    randomProjectionsWithNNet(propaneDataReader.propaneDataSet, ::absoluteError, nnetParams, 10, 10)
}

fun rpIrisStepTwo() {
    val nnetParams: Map<String, Any> = NeuralNetClassifier.createParams(intArrayOf(13), 0.0078f, 500)


    val irisDataReader = IrisDataReader()

    randomProjectionsWithNNet(irisDataReader.irisDataSet, ::absoluteError, nnetParams, 2, 10)
}

fun randomProjectionsWithBoosting(dataSet: DataSet<Instance>, errorFunction: (Double) -> Double, numFeaturesOut: Int) {
    val booster = BoostingClassifier()

    val boostingParams = BoostingClassifier.BoostingParams()

//    val randomProjections = RandomProjectionsWrapper(dataSet, booster, )

    val iterations = 100

    val numFeaturesOut = MLUtils.getNumInputs(dataSet) / 8

//    val bestProjectedDataSet = randomProjections.findBestRandomProjection(numFeaturesOut, iterations)


    System.out.println("original Dataset")
    val originalDataSetTime = timeThis { learningCurveREPBoosting(dataSet, ::absoluteError) }
    System.out.println("elapsed time: $originalDataSetTime")


    System.out.println("random projection dataset")
//    val randomProjectionDataSetTime = timeThis { learningCurveREPBoosting(bestProjectedDataSet, ::absoluteError) }
//    System.out.println("elapsed time: $randomProjectionDataSetTime")
}

fun randomProjectionsWithNNet(dataSet: DataSet<Instance>, errorFunction: (Double) -> Double, nnetParams: Map<String, Any>, numFeaturesOut: Int, iterations: Int) {
    val nNet = NeuralNetClassifier()

    nNet.setParams(nnetParams)

//    val randomProjections = RandomProjectionsWrapper(dataSet, nNet, errorFunction)

//    val bestProjectedDataSet = randomProjections.findBestRandomProjection(numFeaturesOut, iterations)

    System.out.println("original dataset")
    val originalDataSetTime = timeThis { neuralNetLearningCurve(dataSet, errorFunction, nnetParams) }
    System.out.println("elapsed time: $originalDataSetTime")

    System.out.println("random projection dataset")
//    val randomProjectionSetTime = timeThis { neuralNetLearningCurve(bestProjectedDataSet, errorFunction, nnetParams) }
//    System.out.println("elapsed time: $randomProjectionSetTime")

//    System.out.println(bestProjectedDataSet)
}
