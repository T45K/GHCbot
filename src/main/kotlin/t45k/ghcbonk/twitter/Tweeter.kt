package t45k.ghcbonk.twitter

import t45k.ghcbonk.github.ContributionData
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import java.util.Properties

class Tweeter(properties: Properties) {
    private val twitter: Twitter

    init {
        val apiKey: String = properties.getProperty("apiKey")
        val apiSecret: String = properties.getProperty("apiSecret")
        val token: String = properties.getProperty("token")
        val tokenSecret: String = properties.getProperty("tokenSecret")

        this.twitter = TwitterFactory().instance
        twitter.setOAuthConsumer(apiKey, apiSecret)
        val accessToken = AccessToken(token, tokenSecret)
        twitter.oAuthAccessToken = accessToken
    }

    fun tweet(data: ContributionData) {
        this.twitter.updateStatus(getContents(data))
    }

    private fun getContents(data: ContributionData): String {
        return """
            |${data.date}のコントリビューション数: ${data.numOfYesterdayContribution}
            |連続コントリビューション日数: ${data.numOfContinuousContribution}
            |https://github.com/${data.userName}
            """.trimMargin()
    }
}