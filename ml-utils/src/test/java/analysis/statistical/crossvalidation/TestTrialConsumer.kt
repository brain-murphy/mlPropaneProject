package analysis.statistical.crossvalidation

import algorithms.classifiers.DecisionTreeClassifier
import analysis.statistical.errorfunction.AvgAbsoluteError
import analysis.statistical.errorfunction.ErrorFunction
import datasets.Instance
import datasets.IrisDataReader
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.SynchronousQueue

const val NUM_THREADS = 3
const val NUM_TRIALS = 10
const val MOCK_TRIAL_RUNTIME: Long = 100

class TestTrialConsumer {
    private var taskQueue = ArrayBlockingQueue<Trial>(NUM_THREADS)
    private var validationResultsQueue = ArrayBlockingQueue<Double>(NUM_TRIALS)
    private var trainingResultsQueue = ArrayBlockingQueue<Double>(NUM_TRIALS)
    private var consumer = createConsumer()


    @Before
    fun setUp() {
        consumer = createConsumer()
    }

    private fun createConsumer(): TrialConsumer {
        return TrialConsumer(taskQueue, validationResultsQueue, trainingResultsQueue, {})
    }

    @Test
    fun testConsuming() {
        consumer.start()
        for (i in 0..NUM_TRIALS) {
            taskQueue.put(MockTrial())
        }

        val longEnoughToRunTrials = MOCK_TRIAL_RUNTIME * (NUM_TRIALS + 1L)
        Thread.sleep(longEnoughToRunTrials)

        Assert.assertEquals("results should have $NUM_TRIALS entries", NUM_TRIALS, validationResultsQueue.size)
    }


    class MockTrial() : Trial(IrisDataReader().irisDataSet, listOf<Instance>(),
            DecisionTreeClassifier(), AvgAbsoluteError() as ErrorFunction<Number>) {

        override fun run(): Pair<Double, Double> {
            Thread.sleep(MOCK_TRIAL_RUNTIME)

            return Pair(Math.random(), Math.random())
        }
    }
}
