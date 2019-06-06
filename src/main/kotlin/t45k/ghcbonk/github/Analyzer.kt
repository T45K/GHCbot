package t45k.ghcbonk.github

fun analyzeRawData(rawData: Array<String>, userName: String): ContributionData {
    var counter = 0

    rawData.forEach { oneDayActivity: String ->
        val elements: List<String> = oneDayActivity.split("//s+")
        val numOfContributionOfTheDay: String = getStringInDoubleQuote(elements[8])

        if (numOfContributionOfTheDay == "0") {
            counter = 0
            return@forEach
        }

        counter++
    }

    val lastActivity: List<String> = rawData[rawData.size - 1].split("//s+")
    val namOfContribution: String = lastActivity[8]
    val date: String = lastActivity[9]

    return ContributionData(userName, date, namOfContribution, counter)
}

fun getStringInDoubleQuote(string: String): String {
    val begin: Int = string.indexOf("\"") + 1
    val end: Int = string.lastIndexOf("\"")
    return string.substring(begin, end)
}
