package analysis.statistical.errorfunction

import org.junit.Assert
import org.junit.Before
import org.junit.Test


public class TestRunningAverage {
    var runningAverage = RunningAverage()

    @Before
    fun setUp() {
        runningAverage = RunningAverage()
    }

    @Test
    fun testAverage() {
        for (i in 1..3) {
            runningAverage.record(i.toDouble())
        }

        val roundingError = 0.00000001

        Assert.assertEquals(runningAverage.average, 2.0, roundingError)
    }
}