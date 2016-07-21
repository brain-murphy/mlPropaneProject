package logging

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.concurrent.thread


class LoggingTests {

    private val TEST_LOG_TITLE = "A Test Log"


    private val jUnitTestLock = Any()
    private val loggingLock = Any()


    private var nextNumber: Int = 0


    @Test
    fun testBasicLogging() {
        synchronized(jUnitTestLock) {
            val log = Log(TEST_LOG_TITLE)

            Archive.streamLog(log)

            Assert.assertEquals("basic log message couldn't be retreived.", log.time, Archive.logs.last.time)
        }
    }

    @Test
    fun testAsynchronousLogging() {
        synchronized(jUnitTestLock) {

            val arbitraryThreadcount = 10
            val logsPerThread = 100;
            for (i in 0..arbitraryThreadcount) {
                thread {
                    (0..logsPerThread).forEach {
                        streamLogSync()
                    }
                }
            }

            var expectedLogOrder = 0
            Archive.logs.forEach {
                Assert.assertEquals("logs not in correct order.", expectedLogOrder++, it.title.toInt())
            }

        }
    }

    fun getNextNumber(): Int {
        return nextNumber++
    }

    fun streamLogSync() {
        synchronized(loggingLock) {
            Archive.streamLog(Log("${getNextNumber()}"))
        }
    }
}
