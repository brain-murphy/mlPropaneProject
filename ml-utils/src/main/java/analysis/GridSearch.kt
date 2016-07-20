@file:JvmName("GridSearch")

package analysis


class GridSearch<T: Comparable<Number>>(vararg val dimensions: Dimension<T>, val scoreFunction: (Array<Comparable<Number>>) -> Double) {

    public fun findMaxScore(): Array<T> {

        val currentParams = Array<Comparable<Number>>(dimensions.size, {i -> dimensions[i].start})

        var bestScore: Double = Double.NEGATIVE_INFINITY;
        var bestParams: Array<Comparable<Number>>? = null

        while (!isDone(currentParams)) {
            val score = scoreFunction(currentParams);

            if (score >= bestScore) {
                bestParams = currentParams
                bestScore = score
            }


            incrementParams(currentParams)
        }

        if (bestParams != null) {
            return bestParams as Array<T>
        } else {
            throw RuntimeException("no valid scores found")
        }
    }

    public fun findMinScore(): Array<T> {

        val currentParams = Array<Comparable<Number>>(dimensions.size, {i -> dimensions[i].start})

        var bestScore: Double = Double.POSITIVE_INFINITY
        var bestParams: Array<Comparable<Number>>? = null

        while (!isDone(currentParams)) {
            val score = scoreFunction(currentParams);

            if (score >= bestScore) {
                bestParams = currentParams
                bestScore = score
            }


            incrementParams(currentParams)
        }

        if (bestParams != null) {
            return bestParams as Array<T>
        } else {
            throw RuntimeException("no valid scores found")
        }
    }

    private fun incrementParams(params: Array<Comparable<Number>>) {
        var overflowedMax = true;
        var dimension = 0;

        while (overflowedMax) {
            if (params[dimension] < dimensions[dimension].end as Number) {
                params[dimension] = dimensions[dimension].changeFunction(params[dimension])
                overflowedMax = false;

            } else {
                params[dimension] = dimensions[dimension].start
                dimension += 1
            }
        }
    }

    private fun isDone(currentParams: Array<Comparable<Number>>): Boolean {
        for (i in currentParams.indices) {
            if (currentParams[i] <= dimensions[i].end as Number) {
                return false;
            }
        }
        return true;
    }

    public data class Dimension<N : Comparable<Number>>(val start: N, val changeFunction: (Comparable<Number>) -> Comparable<Number>, val end: N)
}

