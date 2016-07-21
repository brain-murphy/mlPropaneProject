package logging

import java.io.Serializable

class Log(val title: String) : Serializable {
    val time = System.nanoTime()
}