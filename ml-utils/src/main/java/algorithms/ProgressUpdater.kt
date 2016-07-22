package algorithms

data class Progress(val proportionComplete: Double? = null,
                    val trainingError:Double? = null)

interface ProgressUpdater {
    fun getProgress(): Progress
}