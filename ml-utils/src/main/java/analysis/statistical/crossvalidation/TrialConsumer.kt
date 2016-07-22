package analysis.statistical.crossvalidation

import algorithms.classifiers.Classifier
import algorithms.classifiers.DecisionTreeClassifier
import analysis.statistical.errorfunction.AvgAbsoluteError
import analysis.statistical.errorfunction.ErrorFunction
import datasets.Instance
import datasets.IrisDataReader
import logging.AsyncLogStream
import logging.logs.DataLog
import logging.logs.DebugLog
import util.async.Consumer
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.SynchronousQueue


class TrialConsumer(private val trialQueue: ArrayBlockingQueue<Trial>,
                    private val resultsQueue: ArrayBlockingQueue<Double>,
                    private val finishedCallback: () -> Unit) : Thread(), Consumer {

    override fun startConsuming() {
        start()
    }

    @Volatile private var shouldContinue = true

    override fun run() {
        try {
            while (shouldContinue) {
                val trial = trialQueue.take()

                resultsQueue.put(trial.run())

                finishedCallback()
            }
        } catch (e: InterruptedException) {
            DebugLog("had to interrupt TrialConsumer")
        }
    }

    override fun stopConsuming() {
        shouldContinue = false
        this.interrupt()
    }
}