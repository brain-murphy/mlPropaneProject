package datasets

import util.Csv
import java.util.*

class DataSet<T : Instance>(private val instances: Array<T>, private val hasDiscreteOutput: Boolean) : Iterable<T> {

    fun getInstances(): Array<T> {
        return instances
    }

    fun splitDataSetRandomly(ways: Int): Array<List<T>> {
        val divisions = Array(ways, { mutableListOf<T>()})

        val instanceList = ArrayList(Arrays.asList(*instances))

        for (i in instances.indices) {
            divisions[i % divisions.size].add(instanceList.removeAt((Math.random() * instanceList.size).toInt()))
        }

        return divisions.toList().toTypedArray()
    }

    override fun iterator(): Iterator<T> {
        return DataSetIterator()
    }

    fun hasDiscreteOutput(): Boolean {
        return hasDiscreteOutput
    }

    override fun toString(): String {
        val numInputs = instances[0].input.size

        val inputColumnNames = (1..numInputs).map { i -> "Input${i}" }.toTypedArray()

        val csv = Csv(*inputColumnNames, "Output")

        instances.forEach { csv.addRow(*it.input.toTypedArray(), it.output) }

        return csv.toString()
    }

    fun deepCopy(): DataSet<T> {

        instances.map {  }

        return DataSet(instances, hasDiscreteOutput)
    }

    private inner class DataSetIterator : Iterator<T> {

        private var index: Int = 0

        override fun hasNext(): Boolean {
            return index < instances.size
        }

        override fun next(): T {
            return instances[index++]
        }
    }
}
