package algorithms.parsers

import datasets.DataSet
import datasets.Instance


interface Clusterer {
    public fun cluster(dataSet: DataSet<Instance>): DataSet<Instance>
}
