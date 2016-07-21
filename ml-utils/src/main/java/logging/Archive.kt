package logging

import datasets.DataSet
import datasets.Instance
import weka.experiment.Experiment
import java.io.Serializable
import java.util.concurrent.SynchronousQueue
import java.util.*
import java.util.concurrent.TimeUnit

object Archive : Serializable {
    var timeout = 10000L

    val dataSet: DataSet<Instance>? = null

    val logs = LinkedList<Log>()

    fun streamLog(log: Log) {
        synchronized(logs, {
            logs.add(log);
        })
    }
}

