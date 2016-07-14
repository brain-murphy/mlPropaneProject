package filters

import datasets.DataSet
import datasets.parsers.AbagailParser
import shared.filt.RandomizedProjectionFilter


fun main(args: String) {

}

const val KEY_NUM_FEATURES_OUT = "num features out param"

class RandomizedProjectionFilter(): Filter {

    var numFeaturesOut = 2

    override fun setParams(params: MutableMap<String, Any>?) {

    }

    override fun filterDataSet(input: DataSet<Instance>?): DataSet<*> {
        val numFeaturesIn = getNumFeaturesIn(input)

        val randomizedProjections = RandomizedProjectionFilter(numFeaturesOut, numFeaturesIn)

        val parser = AbagailParser()

        val abagailDataSet = parser.toAbagailDataSet(input)

        randomizedProjections.filter(abagailDataSet)

        return parser.backToMyDataSetFormat(abagailDataSet)
    }

    private fun getNumFeaturesIn(input: DataSet<*>?): Int {
        val firstInstance = input!!.getInstances()[0]
        val numFeaturesIn = firstInstance.input.size
        return numFeaturesIn
    }

    override fun filterInstance(instance: Instance?): Instance {
        throw UnsupportedOperationException("not implemented")
    }
}