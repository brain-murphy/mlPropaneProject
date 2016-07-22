package analysis.statistical.crossvalidation

import algorithms.classifiers.DecisionTreeClassifier
import analysis.statistical.errorfunction.AvgAbsoluteError
import analysis.statistical.errorfunction.ErrorFunction
import datasets.IrisDataReader
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import util.async.Consumer
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.SynchronousQueue


class TestTrialProducer {
    private val LONG_ENOUGH_TO_FILL_QUEUE = 1000L
    private val QUEUE_SIZE = 4
    private val numFolds = 10

    var queue = ArrayBlockingQueue<Trial>(QUEUE_SIZE)
    var consumers = SynchronousQueue<Consumer>()
    var producer = createProducer(queue, consumers)

    @Before
    fun setUp() {
        queue = ArrayBlockingQueue(QUEUE_SIZE)
        consumers = SynchronousQueue()
        producer = createProducer(queue, consumers)
    }

    private fun createProducer(pQueue: ArrayBlockingQueue<Trial>, pConsumers: SynchronousQueue<Consumer>): TrialProducer {
        val dataSet = IrisDataReader().irisDataSet

        val decisionTreeParams = DecisionTreeClassifier.DecisionTreeParams(DecisionTreeClassifier.Type.C45)

        val errorFunction = AvgAbsoluteError() as ErrorFunction<Number>

        return TrialProducer(dataSet, numFolds, DecisionTreeClassifier().javaClass,
                decisionTreeParams, errorFunction, pQueue)
    }

    @Test
    fun testFillingQueue() {
        producer.start()

        Thread.sleep(LONG_ENOUGH_TO_FILL_QUEUE)

        Assert.assertEquals("should have filled queue by now", QUEUE_SIZE, queue.size)
    }
}