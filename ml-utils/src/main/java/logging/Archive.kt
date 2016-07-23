package logging

import datasets.DataSet
import datasets.Instance
import logging.logs.Log
import util.GeneralUtils
import java.io.Serializable
import java.util.*
import java.util.concurrent.locks.ReentrantLock

object Archive : AsyncLogStream()

open class AsyncLogStream(val shouldForwardToArchive: Boolean = false) : Serializable {
    val dataSet: DataSet<Instance>? = null

    val logs = LinkedList<Log>()

    private val lock = ReentrantLock(true)

    val lastLog: Log
    get() {
        lock.lock()
        val lastLog = logs.last
        lock.unlock()
        return lastLog
    }

    open fun streamLog(log: Log) {
        lock.lock()
            logs.add(log)

            if (shouldForwardToArchive && this != Archive) {
                Archive.streamLog(log)
            }
        lock.unlock()
    }

    fun toFile(fileName: String) {
        GeneralUtils.serializeObject(fileName, this)
    }
}

