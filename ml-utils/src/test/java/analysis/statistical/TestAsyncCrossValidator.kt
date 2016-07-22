package analysis.statistical

import algorithms.classifiers.DecisionTreeClassifier
import datasets.IrisDataReader
import org.junit.Before


class TestAsyncCrossValidator {

    val cv = generateCrossValidator()

    private fun generateCrossValidator() : AsyncCrossValidator {
        val dataSet = IrisDataReader().irisDataSet

        val algorithm = DecisionTreeClassifier()
        algorithm.setParams()

        val newCv = AsyncCrossValidator(dataSet)

    }

    @Before
    fun setUp() {

    }
}