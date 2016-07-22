package logging.logs

import analysis.infrastructure.Experiment
import analysis.statistical.ElapsedTime
import datasets.DataSet
import datasets.Instance


class ResultLog(val result: Experiment.Result, resultOf: Experiment) : DataLog() {}

class StatLog(statistics: Any, resultOf: Experiment) : DataLog() {}

class TimeLog constructor(val message: String, val elapsedTime: Long) : DataLog() {}

inline fun timeThis(message: String, functionToTime: () -> Unit) {
    val timer = ElapsedTime()

    timer.start()
    functionToTime()
    val elapsedTime = timer.stop()

    TimeLog(message, elapsedTime).send()
}