import algorithms.classifiers.Classifier
import algorithms.classifiers.KNearestNeighborsClassifier
import analysis.statistical.AsyncLearningCurve
import analysis.statistical.LearningCurve
import analysis.statistical.crossvalidation.AsyncCrossValidator
import analysis.statistical.errorfunction.AvgAbsoluteError
import analysis.statistical.errorfunction.ErrorFunction
import datasets.DataSet
import datasets.Instance
import datasets.PropaneDataReader
import util.Csv

fun main(args: Array<String>) {
    val dataSet = PropaneDataReader().propaneDataSet

    println(knnLearningCurve(dataSet))
//    testAllDataSetsOnKnn()
}

fun knnError(dataSet: DataSet<Instance>): Double {
    val classifier = (KNearestNeighborsClassifier() as Classifier).javaClass

    val twoNearestNeighbors = 2

    val params = KNearestNeighborsClassifier.KnnParams(twoNearestNeighbors)

    val errorFunction = AvgAbsoluteError() as ErrorFunction<Number>

    val cv = AsyncCrossValidator(dataSet, classifier, params, errorFunction, numFolds = dataSet.getInstances().size)

    return cv.run().second
}

fun knnLearningCurve(dataSet: DataSet<Instance>): Csv {
    val classifier = (KNearestNeighborsClassifier() as Classifier).javaClass

    val twoNearestNeighbors = 2
    val params = KNearestNeighborsClassifier.KnnParams(twoNearestNeighbors)


    val learningCurve = AsyncLearningCurve(dataSet, classifier, params, AvgAbsoluteError().asErrorFunction())


    return learningCurve.run()
}

fun testAllDataSetsOnKnn() {
    for (dataSet in PropaneDataReader().allDataSets) {
        println(knnError(dataSet))
    }
}
