package filters

import algorithms.Algorithm
import datasets.DataSet
import datasets.Instance
import java.util.concurrent.CountDownLatch
import kotlin.concurrent.thread


class RandomizedProjectionsWrapper(private val dataSet: DataSet<Instance>, private val algorithm: Algorithm) {

    fun findBestRandomProjection(numFeaturesOut: Int, maxIterations: Int): DataSet<Instance> {


        for (i in 1..maxIterations) {
            val randomizedProjection = RandomizedProjectionFilter()

            randomizedProjection.numFeaturesOut = numFeaturesOut



        }



    }
}