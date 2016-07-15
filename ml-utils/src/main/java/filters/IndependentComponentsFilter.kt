package filters

import datasets.DataSet
import datasets.Instance
import datasets.parsers.AbagailParser
import shared.filt.IndependentComponentAnalysis

class IndependentComponentsFilter() : Filter {

    override fun setParams(params: MutableMap<String, Any>?) {

    }

    override fun filterDataSet(input: DataSet<Instance>?): DataSet<Instance> {
        val parser = AbagailParser()

        val abagailDataSet = parser.toAbagailDataSet(input)

        val ica = IndependentComponentAnalysis(abagailDataSet)

        ica.filter(abagailDataSet)

        return parser.backToMyDataSetFormat(abagailDataSet)
    }

    override fun filterInstance(instance: Instance?): Instance {
        throw UnsupportedOperationException("not implemented")
    }

}
