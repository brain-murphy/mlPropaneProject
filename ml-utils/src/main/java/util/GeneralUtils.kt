@file:JvmName("GeneralUtils")

package util

import analysis.ElapsedTime
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.*
import java.nio.charset.Charset

object GeneralUtils {

    fun getResourceNamesInPackage(path: String): List<String> {
        val fileNames = mutableListOf<String>()

        try {
            val inputStream = getResourceAsStream(path)
            val br = BufferedReader(InputStreamReader(inputStream))

            var resource: String? = br.readLine()

            while (resource != null) {
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
        val inputStream = getContextClassLoader().getResourceAsStream(resource);

        return inputStream ?: inputStream.javaClass.getResourceAsStream(resource)
    }

    private fun getContextClassLoader(): ClassLoader {
        return javaClass.classLoader
    }

    fun getCsvParser(csvContent: String): CSVParser {

        try {
            return CSVParser.parse(csvContent, CSVFormat.DEFAULT)
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

    fun readFromPackage(filePath: String) : String {
        val inputStream = javaClass.getResourceAsStream(filePath)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val content = reader.readText()

        reader.close()
        inputStream.close()

        return content
    }

    fun timeThis(function: () -> Unit): Double {
        ElapsedTime.tic()
        function()
        return ElapsedTime.toc()
    }
}


