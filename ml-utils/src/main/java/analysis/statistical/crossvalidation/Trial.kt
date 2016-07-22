package analysis.statistical.crossvalidation

import algorithms.classifiers.Classifier
import analysis.statistical.Result
import analysis.statistical.errorfunction.ErrorFunction
import datasets.DataSet
import datasets.Instance

open class Trial(val trainingSet: DataSet<Instance>,
                      val validationSet: List<Instance>,
                      val classifier: Classifier,
                      val errorFunction: ErrorFunction<Number>) {


    open fun run(): Double {
        classifier.train(trainingSet)
        return evaluate()
    }

    private fun evaluate(): Double {
        for (instance in validationSet) {
            val absoluteError = instance.getDifference(classifier.evaluate(instance.input) as Double)
            errorFunction.recordError(absoluteError)
        }

        return errorFunction.getError()
    }

}