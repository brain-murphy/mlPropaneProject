package logging

import javafx.scene.shape.Arc
import logging.logs.DebugLog
import logging.logs.Log
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread


class TestLogging {

    private val TEST_LOG_TEXT = "A Test Log"

    private val jUnitTestLock = ReentrantLock()
    private val loggingLock = ReentrantLock()

    private var nextNumber: Int = 0

//    @Test
    fun testBasicLogging() {
        jUnitTestLock.lock()

            val log = DebugLog(TEST_LOG_TEXT)

            Archive.streamLog(log)
            Assert.assertEquals("basic logs message couldn't be retreived.", log.time, Archive.lastLog.time)

        jUnitTestLock.unlock()
    }

//    @Test
    fun testAsynchronousLogging() {
        jUnitTestLock.lock()

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
                Assert.assertEquals("logs not in correct order.", expectedLogOrder++, (it as DebugLog).message.toInt())
            }

        jUnitTestLock.unlock()
    }

    fun getNextNumber(): Int {
        return nextNumber++
    }

    fun streamLogSync() {
        loggingLock.lock()
            Archive.streamLog(DebugLog("${getNextNumber()}"))
        loggingLock.unlock()
    }

//    @Test
    fun testSendingLog() {
        jUnitTestLock.lock()

            val log = DebugLog(TEST_LOG_TEXT)
            log.send()

            Assert.assertEquals("log should have sent itself", log.time, Archive.lastLog.time)

        jUnitTestLock.unlock()
    }
}
