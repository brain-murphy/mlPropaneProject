package analysis.statistical.crossvalidation

import algorithms.Algorithm
import algorithms.classifiers.Classifier
import analysis.statistical.errorfunction.ErrorFunction
import analysis.statistical.errorfunction.RunningAverage
import datasets.DataSet
import datasets.Instance
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.CountDownLatch


class AsyncCrossValidator(dataSet: DataSet<Instance>,
                          classifier: Class<Classifier>,
                          params: Algorithm.Params,
                          modelErrorFunction: ErrorFunction<Number>,
                          numFolds: Int = 10,
                          maxThreads: Int = 3) {

    private val trialQueue = ArrayBlockingQueue<Trial>(maxThreads)
    private val resultsQueue = ArrayBlockingQueue<Double>(numFolds)

    private val producer = TrialProducer(dataSet, numFolds, classifier, params, modelErrorFunction, trialQueue)

    private val countDownLatch = CountDownLatch(numFolds)
    private val trialFinishedCallback: () -> Unit = {
        countDownLatch.countDown()
    }

    private val consumers = Array(maxThreads) { TrialConsumer(trialQueue, resultsQueue, trialFinishedCallback)}


    fun run(): Double {
        producer.start()
        consumers.forEach { it.start() }

        countDownLatch.await()

        consumers.forEach { it.stopConsuming() }

        return averageResults()
    }

    private fun averageResults(): Double {
        val runningAverage = RunningAverage()
        for (error in resultsQueue) {
            runningAverage.record(error)
        }
        return runningAverage.average
    }
}