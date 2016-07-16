package filters

import datasets.DataSet
import datasets.Instance
import datasets.parsers.AbagailParser
import datasets.parsers.SupervisedAbagailParser
import shared.filt.LinearDiscriminantAnalysis

class LinearDiscriminantFilter : Filter {

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