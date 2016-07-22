@file:JvmName("IcaPropaneScript")

package main

import algorithms.filters.IndependentComponentsFilter
import analysis.infrastructure.Experiment
import analysis.infrastructure.Linker
import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import datasets.PropaneDataReader
import logging.Archive
import logging.logs.ResultLog
import util.GeneralUtils

fun main(args: Array<String>) {
    if (args.size > 0 && args[0].equals("-t")) {
        runIca(IrisDataReader().irisDataSet)
    } else {
        runIca(PropaneDataReader().propaneDataSet)
    }
}

fun runIca(dataSet: DataSet<Instance>) {

    val experiment = Experiment(filters = IndependentComponentsFilter())

    val linker = Linker(dataSet)
    linker.addExperiment(experiment)

    linker.processLinks()

    val resultLog = Archive.logs.first({ log -> log is ResultLog }) as ResultLog

    println(resultLog.result.resultDataSet)

    GeneralUtils.writeToFile("icaPropaneData.csv", resultLog.result.resultDataSet.toString())

    Archive.toFile("propaneIcaLog.ser")
}
