@file:JvmName("Steps")

package main

fun main(args: Array<String>) {
    println("steps entry point works!")
}

private fun printWekaMessage(algorithmName: String) {
    System.out.println("$algorithmName feature transformation done in Weka")
}


fun parseArgs(args: Array<String>): Int {
    if (args.size != 2) {
        printHelp()
        return -1
    } else {
        return Integer.parseInt(args[args.size - 1])
    }
}

private const val HELP_TEXT = "to run a step, use the -s argument. For example: java -jar mlproj2.jar -s 2"

fun printHelp() {
    System.out.println(HELP_TEXT)
}
