package filters

import com.sun.xml.internal.fastinfoset.util.StringArray
import datasets.PropaneDataReader
import util.Csv
import util.GeneralUtils.writeToFile

fun main(args: Array<String>) {
combineAllPropaneData()
}

private val propaneDataReader = PropaneDataReader()

fun combineAllPropaneData() {
    val allInstances = propaneDataReader.propaneDataSet;

    val csv = Csv(*getHeaderLine())
    for (instance in allInstances) {
        csv.addRow(*instance.input.toTypedArray(), instance.output)
    }

    writeToFile("datasets/allPropaneData.csv", csv.toString())
}

private fun getHeaderLine(): Array<String> {
    val frequencies = propaneDataReader.frequencies
    return (frequencies.map { ele -> "${ele}hz"  }.toTypedArray()) + arrayOf("weight")
}
