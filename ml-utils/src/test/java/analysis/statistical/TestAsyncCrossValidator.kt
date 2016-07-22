package analysis.statistical

import algorithms.classifiers.DecisionTreeClassifier
import analysis.statistical.crossvalidation.AsyncCrossValidator
import analysis.statistical.crossvalidation.SyncCrossValidation
import analysis.statistical.errorfunction.AvgAbsoluteError
import analysis.statistical.errorfunction.ErrorFunction
import datasets.IrisDataReader
import org.junit.Before
import org.junit.Test


class TestAsyncCrossValidator {

    var cv = generateCrossValidator()

    private fun generateCrossValidator() : AsyncCrossValidator {
        val dataSet = IrisDataReader().irisDataSet

        val algorithm = DecisionTreeClassifier()

        val params = DecisionTreeClassifier.DecisionTreeParams(DecisionTreeClassifier.Type.C45)

        return AsyncCrossValidator(dataSet, algorithm.javaClass, params, AvgAbsoluteError() as ErrorFunction<Number>,
                numFolds = 20, maxThreads = 3)
    }

    @Before
    fun setUp() {
        cv = generateCrossValidator()
    }

    @Test
    fun testRunning() {
        println(cv.run())
    }

    @Test
    fun testNormalCv() {
        val algorithm = DecisionTreeClassifier()
        algorithm.setParams(DecisionTreeClassifier.DecisionTreeParams(DecisionTreeClassifier.Type.C45))

        println(SyncCrossValidation(SyncCrossValidation.AbsoluteError(), 20, IrisDataReader().irisDataSet, algorithm))
    }
}