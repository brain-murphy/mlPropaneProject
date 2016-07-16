package filters

import datasets.DataSet
import datasets.Instance
import shared.filt.LinearDiscriminantAnalysis

class LinearDiscriminantFilter : Filter {

    val lda = LinearDiscriminantAnalysis()
    override fun filterDataSet(input: DataSet<Instance>?): DataSet<Instance> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun filterInstance(instance: Instance?): Instance {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setParams(params: MutableMap<String, Any>?) {
        throw UnsupportedOperationException("not implemented")
    }

}