package logging

import datasets.DataSet
import datasets.Instance
import logging.logs.Log
import util.GeneralUtils
import java.io.Serializable
import java.util.*

object Archive : Serializable {
    var timeout = 10000L

    val dataSet: DataSet<Instance>? = null

    val logs = LinkedList<Log>()

    fun streamLog(log: Log) {
        synchronized(logs, {
            logs.add(log)
        })
    }

    fun toFile(fileName: String) {
        GeneralUtils.serializeObject(fileName, this)
    }
}

