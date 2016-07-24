package analysis.statistical.crossvalidation

import algorithms.classifiers.Classifier
import analysis.statistical.Result
import analysis.statistical.errorfunction.ErrorFunction
import datasets.DataSet
import datasets.Instance

open class Trial(val trainingSet: DataSet<Instance>,
                      val validationSet: List<Instance>,
                      val classifier: Classifier,
                      var errorFunction: ErrorFunction<Number>) {


    open fun run(): Pair<Double, Double> {
        classifier.train(trainingSet)
        return Pair(evaluate(trainingSet.getInstances().toList()), evaluate(validationSet))
    }

    private fun evaluate(data: List<Instance>): Double {
        errorFunction = errorFunction.javaClass.newInstance()
        for (instance in data) {
            val absoluteError = instance.getDifference(classifier.evaluate(instance.input) as Double)
            errorFunction.recordError(absoluteError)
        }

        return errorFunction.getError()
    }

}