package analysis.statistical.errorfunction


open class ErrorFunction<in T : Number>(private val function: (T) -> Double) {
    protected val runningAverage = RunningAverage()

    fun recordError(difference: T) {
        runningAverage.record(function(difference))
    }

    fun getError(): Double {
        return runningAverage.average
    }
}

class AvgAbsoluteError : ErrorFunction<Double>(Math::abs) {

}