package t45k.ghcbonk

import t45k.ghcbonk.github.ContributionData
import t45k.ghcbonk.github.GitHubUser
import t45k.ghcbonk.twitter.Tweeter
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Properties
import java.util.Timer
import java.util.TimerTask

class App {
    companion object {
        const val TIME_OF_ONE_DAY: Long = 1000 * 60 * 60 * 24
    }
}

fun main(args: Array<String>) {
    val properties = Properties()
    properties.load(Files.newBufferedReader(Paths.get(args[0])))

    val timerTask = object : TimerTask() {
        override fun run() {
            val userName: String = properties.getProperty("userName")
            val user = GitHubUser(userName)
            val contributionData: ContributionData = user.fetchContributionData()
            val tweeter = Tweeter(properties)
            tweeter.tweet(contributionData)
        }
    }

    val timer = Timer()
    val date = Date()
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = date
    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    val format: String = simpleDateFormat.format(calendar.time)

    if (format.endsWith("00:00:00")) {
        timer.schedule(timerTask, App.TIME_OF_ONE_DAY)
    } else {
        calendar.add(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        timer.schedule(timerTask, calendar.time, App.TIME_OF_ONE_DAY)
    }
}


