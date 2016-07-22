package util.async


interface Producer {
    fun startProducing()
    fun continueProducing()
    fun stopProducing()
}