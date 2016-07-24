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
    private val validatationResults = ArrayBlockingQueue<Double>(numFolds)
    private val trainingErrorResults = ArrayBlockingQueue<Double>(numFolds)

    private val producer = TrialProducer(dataSet, numFolds, classifier, params, modelErrorFunction, trialQueue)

    private val countDownLatch = CountDownLatch(numFolds)
    private val trialFinishedCallback: () -> Unit = {
        countDownLatch.countDown()
    }

    private val consumers = Array(maxThreads) { TrialConsumer(trialQueue, validatationResults, trainingErrorResults, trialFinishedCallback)}


    fun run(): Pair<Double, Double> {
        producer.start()
        consumers.forEach { it.start() }

        countDownLatch.await()

        consumers.forEach { it.stopConsuming() }

        return averageResults()
    }

    private fun averageResults(): Pair<Double, Double> {
        val validationAverage = RunningAverage()
        for (error in validatationResults) {
            validationAverage.record(error)
        }

        val trainingAverage = RunningAverage()
        for (error in trainingErrorResults) {
            trainingAverage.record(error)
        }
        return Pair(trainingAverage.average, validationAverage.average)
    }
}