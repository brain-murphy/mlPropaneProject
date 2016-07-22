import algorithms.filters.IndependentComponentsFilter
import analysis.infrastructure.Experiment
import analysis.infrastructure.Linker
import datasets.IrisDataReader
import datasets.PropaneDataReader
import logging.Archive
import logging.logs.ResultLog

fun main(args: Array<String>) {
    runIca()
}

fun runIca() {
    val dataSet = PropaneDataReader().propaneDataSet

    val experiment = Experiment(filters = IndependentComponentsFilter())

    val linker = Linker(dataSet)
    linker.addExperiment(experiment)

    linker.processLinks()

    val resultLog = Archive.logs.first({ log -> log is ResultLog }) as ResultLog

    println(resultLog.result.resultDataSet)

    Archive.toFile("irisIcaTest.ser")
}
