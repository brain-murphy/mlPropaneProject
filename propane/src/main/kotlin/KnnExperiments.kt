import algorithms.classifiers.Classifier
import algorithms.classifiers.KNearestNeighborsClassifier
import analysis.statistical.crossvalidation.AsyncCrossValidator
import analysis.statistical.errorfunction.AvgAbsoluteError
import analysis.statistical.errorfunction.ErrorFunction
import datasets.PropaneDataReader

fun main(args: Array<String>) {
    testKnnVisualizationSet()
}

fun testKnnVisualizationSet() {
    val dataSet = PropaneDataReader().rpForVisualization

    val classifier = (KNearestNeighborsClassifier() as Classifier).javaClass

    val twoNearestNeighbors = 2

    val params = KNearestNeighborsClassifier.KnnParams(twoNearestNeighbors)

    val errorFunction = AvgAbsoluteError() as ErrorFunction<Number>

    val cv = AsyncCrossValidator(dataSet, classifier, params, errorFunction)

    println(cv.run())
}
