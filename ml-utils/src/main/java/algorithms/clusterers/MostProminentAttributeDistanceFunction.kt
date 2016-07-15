package algorithms.clusterers

import weka.core.EuclideanDistance
import weka.core.Instance
import weka.core.Instances
import weka.core.neighboursearch.PerformanceStats

class MostProminentAttributeDistanceFunction: EuclideanDistance() {

    override fun distance(first: Instance?, second: Instance?, cutOffValue: Double, stats: PerformanceStats?): Double {
        return Math.abs(getMostProminentAttribute(first!!) - getMostProminentAttribute(second!!)).toDouble()
    }

    private fun getMostProminentAttribute(instance: Instance): Int {
        var weightedAttributeIndexSum = 0.0
        var sumOfWeights = 0.0

        for (attributeIndex in 0..instance.numAttributes() - 1) {
            val weight = Math.pow(instance.value(attributeIndex), 3.0)

            weightedAttributeIndexSum += weight * attributeIndex
            sumOfWeights += weight
        }

        val weightedAverageAttribute = weightedAttributeIndexSum / sumOfWeights

        return Math.round(weightedAverageAttribute).toInt()
    }

    override fun postProcessDistances(distances: DoubleArray?) {

    }

    override fun updateDistance(currDist: Double, diff: Double): Double {
        return currDist
    }

    override fun difference(index: Int, val1: Double, val2: Double): Double {
        throw RuntimeException("difference not defined for individual attributes in MostProminentAttributeDistanceFunction")
    }
}
