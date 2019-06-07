package t45k.ghcbonk.twitter

import t45k.ghcbonk.github.ContributionData
import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.auth.AccessToken
import java.util.*

class Tweeter(properties: Properties) {
    private val twitter: Twitter

    init {
        val apiKey = properties.getProperty("apiKey")
        val apiSecret = properties.getProperty("apiSecret")
        val token = properties.getProperty("token")
        val tokenSecret = properties.getProperty("tokenSecret")

        this.twitter = TwitterFactory().instance
        twitter.setOAuthConsumer(apiKey, apiSecret)
        val accessToken = AccessToken(token, tokenSecret)
        twitter.oAuthAccessToken = accessToken
    }

    fun tweet(data: ContributionData) {
        this.twitter.updateStatus(getContents(data))
    }

    private fun getContents(data: ContributionData): String {
        return (data.date
                + "のコントリビューション数: "
                + data.numOfYersterdayContribution
                + "\n"
                + "連続コントリビューション日数: "
                + data.numOfContinuousContribution
                + "\n"
                + "https://github.com/"
                + data.userName)
    }
}