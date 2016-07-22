package datasets


class MockInstance(private var output: Double = Math.random()) : Instance {

    private var input: DoubleArray = getRandomInput()
    private val numInputs = 5

    private fun  getRandomInput() = DoubleArray(numInputs) { Math.random() }

    override fun getInput(): DoubleArray {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setInput(input: DoubleArray?) {
        if (input != null) {
            this.input = input
        }
    }

    override fun getOutput(): Double {
        return output
    }

    override fun setOutput(output: Double) {
        this.output = output
    }

    override fun getPossibleOutputs(): DoubleArray {
        return arrayOf(0.0, 1.0).toDoubleArray()
    }

    override fun getDifference(computedOutput: Double): Double {
        return output - computedOutput
    }

    override fun deepCopy(): Instance {
        val newInstance = MockInstance()

        newInstance.output = this.output
        newInstance.input = this.input

        return newInstance
    }

}