package analytics.infrastructure

import analysis.infrastructure.Experiment
import analysis.infrastructure.Linker
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import datasets.MockInstance
import logging.Archive
import logging.logs.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class TestLinker {

    private val numMockExperiments = 4
    private val numMockInstances = 50

    var linker = createLinker()

    @Before
    fun setUp() {
        linker = createLinker()
    }

    private fun createLinker() = Linker(IrisDataReader().irisDataSet)

    @Test
    fun testSimpleChain() {
        val mockExperiments = createMockExperiments()

        for ((experiment, result) in mockExperiments) {
            linker.addExperiment(experiment, shouldLogData = true)
        }

        linker.processLinks()

        assertLogsCorrect(mockExperiments)
    }

    private fun assertLogsCorrect(mockExperiments: List<Pair<Experiment, Experiment.Result>>) {
        val logIterator = Archive.logs.iterator()

        for (i in 0..mockExperiments.size - 1) {
            val (experiment, result) = mockExperiments[i]

            Assert.assertTrue("should be debug log at experiment index $i", logIterator.next() is DebugLog)

            Assert.assertTrue("should be timing log at experiment index $i", logIterator.next() is TimeLog)

            Assert.assertTrue("should be debug log for third log in experiment $i", logIterator.next() is DebugLog)

            Assert.assertSame("wrong result at experiment $i", result, (logIterator.next() as ResultLog).result)
        }
    }

    private fun createMockExperiments(): List<Pair<Experiment, Experiment.Result>> {
        val mockExperiments = mutableListOf<Pair<Experiment, Experiment.Result>>()

        for (experimentIndex in 0..numMockExperiments) {
            val mockInstances = Array<Instance>(numMockInstances, { MockInstance(experimentIndex.toDouble())})
            val mockDataSet = DataSet<Instance>(mockInstances, false)

            val mockResult = Experiment.Result(mockDataSet)

            mockExperiments.add(Pair(MockExperiment(mockResult), mockResult))
        }

        return mockExperiments
    }
}

class MockExperiment(val resultToReturn: Result) : Experiment() {

    override fun run(dataSet: DataSet<Instance>): Result {
        return resultToReturn
    }

}