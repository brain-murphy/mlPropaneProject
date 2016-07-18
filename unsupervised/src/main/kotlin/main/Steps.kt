package main

import algorithms.clusterers.KMeansAlgorithm
import clusterers.*
import datasets.IrisDataReader
import datasets.PropaneDataReader
import dimreduction.rpPropaneStepTwo
import dimreduction.printIcaForDataSet
import dimreduction.rpIrisStepTwo

fun main(args: Array<String>) {
    val step = parseArgs(args)

    when (step) {
        (1) -> {
            kMeansIrisDataStepOne()
            kMeansPropaneDataStepOne()
            emIrisDataStepOne()
            emPropaneDataStepOne()
        }

        (2) -> {
            printIcaForDataSet(IrisDataReader().irisDataSet)
            printIcaForDataSet(PropaneDataReader().propaneDataSet)
            rpPropaneStepTwo()
            rpIrisStepTwo()
            printWekaMessage("CFS")
            printWekaMessage("PCA")
        }

        (3) -> {
            System.out.println("This clustering done in Weka, using as input the corresponding files in the datasets folder. The outputs can be found in results/dimensionalityReducedClusters")
        }

        (4) -> {
            System.out.println("See results in results/dimensionalityReducedNNet")
        }

        (5) -> {
            val numClusters = 3
            System.out.println(dimReductionUsingClusters(IrisDataReader().reducedDataSets, numClusters, KMeansAlgorithm.DistanceFunction.Euclidean))
        }

        (6) -> {
            testClusterDataDouble(IrisDataReader().clusteredDataSet)
        }

        else ->
                printHelp()
    }
}

private fun printWekaMessage(algorithmName: String) {
    System.out.println("$algorithmName feature transformation done in Weka")
}


fun parseArgs(args: Array<String>): Int {
    if (args.size != 2) {
        printHelp()
        return -1
    } else {
        return Integer.parseInt(args[args.size - 1])
    }
}

private const val HELP_TEXT = "to run a step, use the -s argument. For example: java -jar mlproj2.jar -s 2"

fun printHelp() {
    System.out.println(HELP_TEXT)
}
