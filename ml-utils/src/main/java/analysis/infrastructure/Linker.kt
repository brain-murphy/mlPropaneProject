package analysis.infrastructure

import datasets.DataSet
import datasets.Instance
import logging.logs.*
import java.util.*

data class Link(val experiment: Experiment, val shouldLogData: Boolean)

class Linker(val dataSet: DataSet<Instance>) {

    private val links: MutableList<Link> = LinkedList()

    fun processLinks() {
        var currentDataSet = dataSet

        links.forEach { currentDataSet = processLink(it, currentDataSet.deepCopy()) }
    }

    private fun processLink(link: Link, currentDataSet: DataSet<Instance>): DataSet<Instance> {
        DebugLog("processing link $link").send()

        var result: Experiment.Result? = null;

        timeThis("elapsed time for experiment ${link.experiment}") { result = link.experiment.run(currentDataSet) }

        DebugLog("finished running experiment ${link.experiment}").send()

        if (link.shouldLogData) {
            ResultLog(result!!, link.experiment).send()
        }

        return result!!.resultDataSet
    }

    fun addExperiment(experiment: Experiment, shouldLogData: Boolean = true) {
        links.add(Link(experiment, shouldLogData))
    }

}