@file:JvmName("GeneralUtils")

package util

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.*
import java.nio.charset.Charset

fun getResourceNamesInPackage(path: String): List<String> {
    val fileNames = mutableListOf<String>()

    try {
        val inputStream = getResourceAsStream(path)
        val  br = BufferedReader(InputStreamReader(inputStream))

        var resource: String? = br.readLine()

        while(resource != null) {
            fileNames.add(resource)
            resource = br.readLine()
        }

    } catch (exception: Exception) {
        exception.printStackTrace()
        throw RuntimeException()
    }

    return fileNames.toList()
}

private fun getResourceAsStream(resource: String): InputStream {
    val inputStream = getContextClassLoader().getResourceAsStream( resource );

    return inputStream ?: inputStream.javaClass.getResourceAsStream( resource )
}

private fun getContextClassLoader(): ClassLoader  {
    return Thread.currentThread().contextClassLoader;
}

fun getCsvParser(filePath: String): CSVParser {

    val csvData = getContextClassLoader().getResource(filePath)
    //        File csvData = new File(PROPANE_DATA_2013_FILE_PATH);

    try {
        return CSVParser.parse(csvData!!, Charset.defaultCharset(), CSVFormat.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    throw RuntimeException("couldn't create parser")
}

fun writeToFile(fileName: String, content: String) {
    val writer = File(fileName).bufferedWriter()
    writer.write(content)
    writer.flush()
    writer.close()
}

fun absoluteError(difference: Double): Double {
    return Math.abs(difference)
}


