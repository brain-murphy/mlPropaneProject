@file:JvmName("PcaExperiments")

import datasets.DataSet
import datasets.Instance
import datasets.PcaPropaneDataReader
import datasets.PropaneDataReader
import util.ElapsedTime

fun compareRbfResults() {
    val pcaPropaneData = PcaPropaneDataReader().propaneDataSet
    val originalPropaneData = PropaneDataReader().propaneDataSet

    ElapsedTime.tic()
    val pcaDataResults = runRbf(pcaPropaneData as DataSet<Instance>, false, 9.765625E-5, 10)
    val pcaElapsedTime = ElapsedTime.toc()

    ElapsedTime.tic()
    val originalDataResults = runRbf(originalPropaneData as DataSet<Instance>, false, 9.765625E-5, 10)
    val originalDataElapsedTime = ElapsedTime.toc()

    println("mean absolute validation error using RBF on original data:" + originalDataResults.meanValidationError + " after time:" + originalDataElapsedTime)
    println("mean absolute validation error using RBF on data with PCA:" + pcaDataResults.meanValidationError + " after time:" + pcaElapsedTime)
}
