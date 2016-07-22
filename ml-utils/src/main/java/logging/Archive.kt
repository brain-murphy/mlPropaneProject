package logging

import datasets.DataSet
import datasets.Instance
import logging.logs.Log
import util.GeneralUtils
import java.io.Serializable
import java.util.*

object Archive : AsyncLogStream()

open class AsyncLogStream(val shouldForwardToArchive: Boolean = false) : Serializable {
    val dataSet: DataSet<Instance>? = null

    val logs = LinkedList<Log>()

    open fun streamLog(log: Log) {
        synchronized(logs, {
            logs.add(log)

            if (shouldForwardToArchive && this != Archive) {
                Archive.streamLog(log)
            }
        })
    }

    fun toFile(fileName: String) {
        GeneralUtils.serializeObject(fileName, this)
    }
}

