package algorithms.filters

import algorithms.Algorithm
import datasets.DataSet
import datasets.Instance
import algorithms.parsers.AbagailParser
import algorithms.parsers.SupervisedAbagailParser
import shared.filt.LinearDiscriminantAnalysis

class LinearDiscriminantFilter : Filter {
    override fun setParams(params: Algorithm.Params?) {
        throw UnsupportedOperationException("not implemented")
    }

    var lda: LinearDiscriminantAnalysis? = null

    override fun filterDataSet(input: DataSet<Instance>?): DataSet<Instance> {
        val parser = SupervisedAbagailParser()

        val abagailDataSet = parser.toAbagailDataSet(input)

        lda = LinearDiscriminantAnalysis(abagailDataSet)

        lda!!.filter(abagailDataSet)

        return parser.backToMyDataSetFormat(abagailDataSet)
    }

    override fun filterInstance(instance: Instance?): Instance {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setParams(params: MutableMap<String, Any>?) {
        throw UnsupportedOperationException("not implemented")
    }

}