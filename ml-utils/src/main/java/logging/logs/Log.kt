package logging.logs

import logging.Archive
import java.io.Serializable

abstract class Log(val title: String) : Serializable {
    val time = System.nanoTime()
    abstract val logLevel: LogLevel

    open fun send() {
        Archive.streamLog(this)
    }
}

class DebugLog(val message: String) : Log("Debug Log"){
    override val logLevel = LogLevel.Debug
}

class ErrorLog(val message: String, throwable: Throwable) : Log("Error Log"){
    override val logLevel = LogLevel.Error
}

open class DataLog() : Log("Data Log") {
    override val logLevel = LogLevel.Data
}

enum class LogLevel {
    Debug,
    Error,
    Data;
}