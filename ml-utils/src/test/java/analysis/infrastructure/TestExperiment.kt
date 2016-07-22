package analysis.infrastructure

import algorithms.classifiers.Classifier
import algorithms.classifiers.DecisionTreeClassifier
import algorithms.clusterers.KMeansClusterer
import algorithms.filters.IndependentComponentsFilter
import analysis.infrastructure.Experiment
import datasets.IrisDataReader
import org.junit.Before
import org.junit.Test

class TestExperiment {

    var experiment = generateExperiment()

    fun generateExperiment(): Experiment {
        val classifier = setupClassifier()

        val clusterer = setupClusterer()

        val filter = IndependentComponentsFilter()

        val experiment = Experiment(classifier,  clusterer, filter)

        return experiment
    }

    private fun setupClusterer(): KMeansClusterer {
        val clusterer = KMeansClusterer()
        clusterer.setParams(KMeansClusterer.createParams(3, KMeansClusterer.DistanceFunction.Manhattan))
        return clusterer
    }

    private fun setupClassifier(): Classifier {
        val classifier = DecisionTreeClassifier()
        classifier.setParams(DecisionTreeClassifier.createParams(DecisionTreeClassifier.Type.RepTree))
        return classifier
    }

    @Before
    fun setUp() {
        experiment = generateExperiment()
    }

    @Test
    fun testRunningExperiment() {
        val dataSet = IrisDataReader().irisDataSet

        experiment.run(dataSet)
    }
}
