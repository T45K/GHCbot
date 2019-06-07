package t45k.ghcbonk.github

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class GitHubUser(private val userName: String) {
    companion object {
        private const val USER_URL_PREFIX = "https://github.com/users/"
        private const val USER_URL_SUFFIX = "/contributions"
    }

    private val userURL: URL

    init {
        this.userURL = URL(USER_URL_PREFIX + userName + USER_URL_SUFFIX)
    }

    fun fetchContributionData(): ContributionData {

        val connection: HttpURLConnection = setConnection()
        val rawData: Array<String> = fetchRawData(connection)

        return analyzeRawData(rawData, userName)
    }

    private fun setConnection(): HttpURLConnection {
        val connection = userURL.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.instanceFollowRedirects = false
        connection.connect()

        return connection
    }

    private fun fetchRawData(connection: HttpURLConnection): Array<String> {
        val status: Int = connection.responseCode
        return if (status == HttpURLConnection.HTTP_OK) {
            readInputStream(connection.inputStream)
        } else {
            emptyArray()
        }
    }

    private fun readInputStream(inputStream: InputStream): Array<String> {
        val inputStreamReader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(inputStreamReader as Reader?)
        return bufferedReader.lines()
                .filter { !isNecessaryInformation(it) }
                .toArray<String> { length -> arrayOfNulls(length) }
    }

    private fun isNecessaryInformation(line: String): Boolean {
        return line.contains("<rect") && !line.contains(getDate())
    }

    private fun getDate(): String {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        return SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN).format(calendar.time)
    }
}
