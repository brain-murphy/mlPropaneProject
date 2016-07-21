package filters

import datasets.PropaneDataReader
import util.Csv
import util.GeneralUtils.getCsvParser
import util.GeneralUtils.getResourceNamesInPackage
import util.GeneralUtils.writeToFile

private const val NEW_SAMPLE_DIRECTORY_NAME = "newsamples"

private val FILTERS: Array<(DoubleArray) -> Boolean> = arrayOf(::fitsOriginalWindow, ::hasSameFeatureCountAsOriginal)

fun main(args: Array<String>) {
    writeToFile("datasets/propaneData2016.csv", consolidateCsvFiles().toString())
}

fun getOriginalFrequencyMagnitudeWindow(): Pair<Double, Double> {
    val dataSet = PropaneDataReader().get2013PropaneDataSet()

    var minPeakFrequency = Double.MAX_VALUE
    var maxPeakFrequency = Double.MIN_VALUE

    for (instance in dataSet.getInstances()) {
        val frequencyMagnitudes = instance.input

        val instanceMaxFrequency = frequencyMagnitudes.max()!!

        maxPeakFrequency = Math.max(instanceMaxFrequency, maxPeakFrequency)
        minPeakFrequency = Math.min(instanceMaxFrequency, minPeakFrequency)
    }

    return Pair(minPeakFrequency, maxPeakFrequency) // (100298.0, 6535971.0)
}

fun consolidateCsvFiles(): Csv {
    val csvFileNames = getResourceNamesInPackage(NEW_SAMPLE_DIRECTORY_NAME)

    val frequencies = PropaneDataReader().frequencies

    val consolidatedCsv = Csv(*(frequencies.map { ele -> "${ele}hz"  }.toTypedArray()), "weight")

    for (fileName in csvFileNames) {
        val csvRecordIterator = getCsvParser(NEW_SAMPLE_DIRECTORY_NAME + "/" + fileName).iterator()

        csvRecordIterator.next() // we don't want the header line to be parsed as data

        for (csvRecord in csvRecordIterator) {
            val row = csvRecord.map { s -> s.toDouble() }.toDoubleArray()

            if (passesFilters(row)) {
                consolidatedCsv.addRow(*row.toTypedArray())
            }
        }
    }

    return consolidatedCsv
}

fun passesFilters(row: DoubleArray): Boolean {

    for (filterFunction in FILTERS) {
        if (!filterFunction(row)) {
            return false
        }
    }

    return true
}

fun fitsOriginalWindow(row: DoubleArray): Boolean {
    val (windowMin, windowMax) = getOriginalFrequencyMagnitudeWindow()

    val rowMax = row.max()!!

    return rowMax > windowMin && rowMax < windowMax
}

fun hasSameFeatureCountAsOriginal(row: DoubleArray): Boolean {

    return row.size == 193
}



