package dimreduction

import datasets.DataSet
import datasets.Instance
import datasets.IrisDataReader
import datasets.PropaneDataReader
import filters.LinearDiscriminantFilter
import util.writeToFile

fun main(args: Array<String>) {
    lda(PropaneDataReader().propaneDataSet)
}

fun lda(dataSet: DataSet<Instance>) {
    val lda = LinearDiscriminantFilter()

    val resultDataSet = lda.filterDataSet(dataSet)

    writeToFile("lda_propaneData", resultDataSet.toString())
}