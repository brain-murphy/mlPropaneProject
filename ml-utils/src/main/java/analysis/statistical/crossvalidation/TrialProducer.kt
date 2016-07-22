package analysis.statistical.crossvalidation

import algorithms.Algorithm
import algorithms.classifiers.Classifier
import analysis.statistical.errorfunction.ErrorFunction
import datasets.DataSet
import datasets.Instance
import sun.misc.Lock
import util.async.Producer
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.atomic.AtomicInteger


class TrialProducer(private val dataSet: DataSet<Instance>,
                    private val numFolds: Int,
                    private val classifierClass: Class<Classifier>,
                    private val classifierParams: Algorithm.Params,
                    private val error: ErrorFunction<Number>,
                    private val queue: ArrayBlockingQueue<Trial>) : Thread() {

    private val folds = dataSet.splitDataSetRandomly(numFolds)

    override fun run() {
        for (foldIndex in 0..numFolds - 1) {
            queue.put(makeTrial(foldIndex))
        }
    }

    private fun makeTrial(foldIndex: Int): Trial {
        val trainingSet = DataSet(combineAllListsExceptOne(folds, foldIndex), dataSet.hasDiscreteOutput())

        val validationSet = folds[foldIndex]

        val classifier = createClassifier()

        return Trial(trainingSet, validationSet, classifier, error.javaClass.newInstance())
    }

    private fun createClassifier(): Classifier {
        val classifier = classifierClass.newInstance()
        classifier.setParams(classifierParams)
        return classifier
    }

    private fun combineAllListsExceptOne(lists: Array<List<Instance>>, listToLeaveOut: Int): Array<Instance> {
        if (lists.size == 1) {
            return lists[0].toTypedArray()
        }

        val combinedArray = arrayOfNulls<Instance>(totalSizeOfAllLists(lists) - lists[listToLeaveOut].size)

        var combinedArrayIndex = 0

        for (listIndex in lists.indices) {
            if (listIndex == listToLeaveOut) {
                continue
            }

            for (instance in lists[listIndex]) {
                combinedArray[combinedArrayIndex] = instance
                combinedArrayIndex += 1
            }
        }

        return combinedArray.requireNoNulls()
    }

    private fun totalSizeOfAllLists(lists: Array<List<Instance>>): Int {
        var total = 0
        for (list in lists) {
            total += list.size
        }

        return total
    }
}