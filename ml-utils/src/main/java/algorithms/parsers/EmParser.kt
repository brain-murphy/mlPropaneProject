package algorithms.parsers

import datasets.DataSet
import datasets.Instance


interface Clusterer {
    fun cluster(dataSet: DataSet<Instance>): DataSet<Instance>
}
