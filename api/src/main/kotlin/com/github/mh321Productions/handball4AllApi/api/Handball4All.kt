package com.github.mh321Productions.handball4AllApi.api

import com.github.mh321Productions.handball4AllApi.api.data.RawData
import com.github.mh321Productions.handball4AllApi.api.data.H4AllResponse
import com.github.mh321Productions.handball4AllApi.api.data.ResponseMenu
import com.github.mh321Productions.handball4AllApi.api.data.endpoints.GameList
import com.github.mh321Productions.handball4AllApi.api.data.endpoints.LeagueInfoContent
import com.github.mh321Productions.handball4AllApi.api.data.endpoints.LeagueInfoHead
import com.github.mh321Productions.handball4AllApi.api.data.endpoints.RawLeagueInfoContent
import com.github.mh321Productions.handball4AllApi.api.data.endpoints.TeamInfo
import com.github.mh321Productions.handball4AllApi.api.data.exceptions.H4AllException
import kotlinx.coroutines.future.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecodingException
import kotlinx.serialization.json.JsonEncodingException
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * The base class for the Handball4all API
 */
object Handball4All {
    private const val BASE_URL = "https://spo.handball4all.de/service/if_g_json.php"
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }
    private val client = HttpClient.newHttpClient()


    /**
     * Queries the information about a league,
     * containing the games, the scores and the ranking.
     *
     * This query allows to show all games during the season.
     *
     * @param organization The organization hosting the league (The `ogId` parameter in the Browser url or [ResponseMenu.org], e.g. `216` for the BWHV)
     * @param leagueId The league id (The `lId` parameter in the Browser url or [GameList.leagueId])
     * @param showAllGames Whether to query all games in the season (they are then all listed under [LeagueInfoContent.futureGames])
     */
    suspend fun getLeagueInfo(organization: Int, leagueId: Int, showAllGames: Boolean = false) = getLeagueInfo(organization, leagueId, null, null, showAllGames)

    /**
     * Queries the information about a league,
     * containing the games, the scores and the ranking.
     *
     * This query allows to specify a week to show all games from.
     *
     * @param organization The organization hosting the league (The `ogId` parameter in the Browser url or [ResponseMenu.org], e.g. `216` for the BWHV)
     * @param leagueId The league id (The `lId` parameter in the Browser url or [GameList.leagueId])
     * @param date The week to query (the week's monday as `YYYY-MM-DD`, see [ResponseMenu.dates] for possible values), or `null` for the current week
     */
    suspend fun getLeagueInfo(organization: Int, leagueId: Int, date: String?) = getLeagueInfo(organization, leagueId, null, date, false)

    /**
     * Queries the information about a league,
     * containing the games, the scores and the ranking.
     *
     * This query shows all games for a specified team under [LeagueInfoContent.futureGames].
     *
     * @param organization The organization hosting the league (The `ogId` parameter in the Browser url or [ResponseMenu.org], e.g. `216` for the BWHV)
     * @param leagueId The league id (The `lId` parameter in the Browser url or [GameList.leagueId])
     * @param teamId The team id (The `tId` parameter in the Browser url or [TeamInfo.teamId])
     */
    suspend fun getLeagueInfo(organization: Int, leagueId: Int, teamId: Int) = getLeagueInfo(organization, leagueId, teamId, null, false)


    private suspend fun getLeagueInfo(organization: Int, leagueId: Int, teamId: Int? = null, date: String? = null, showAllGames: Boolean = false)
    = runRequest<LeagueInfoHead, LeagueInfoContent, LeagueInfoHead, RawLeagueInfoContent>(
        buildMap {
            put("cmd", "ps")
            put("og", organization.toString())
            put("cl", leagueId.toString())
            put("ca", if (showAllGames) "1" else "0")
            if (date != null) { put("do", date) }
            if (teamId != null) { put("ct", teamId.toString()) }
        }
    )

    @OptIn(ExperimentalSerializationApi::class)
    private suspend inline fun <THead, TContent, reified TRawHead : RawData<THead>, reified TRawContent : RawData<TContent>> runRequest(query: Map<String, String>): Result<H4AllResponse<THead, TContent>> {
        val request = HttpRequest.newBuilder(buildUri(query)).GET().build()
        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).await()

        try {
            val rawData = json.decodeFromString<List<H4AllResponse<TRawHead, TRawContent>>>(response.body()).first()
            val highLevelData = H4AllResponse(rawData.header, rawData.menu, rawData.head.getData(), rawData.content.getData(), rawData.footer, rawData.error, rawData.debug)
            return Result.success(highLevelData)
        } catch (jsonException: JsonDecodingException) {
            val exception = try {
                json.decodeFromString<H4AllException>(response.body())
            } catch (_: JsonEncodingException) {
                jsonException
            }

            return Result.failure(exception)
        } catch (otherEx: Exception) {
            return Result.failure(otherEx)
        }
    }

    private fun buildUri(query: Map<String, String>): URI {
        val baseUri = URI(BASE_URL)

        if (query.isEmpty()) return baseUri

        val sb = StringBuilder(baseUri.query ?: "")

        for ((key, value) in query) {
            if (sb.isNotEmpty()) sb.append('&')

            sb.append(URLEncoder.encode(key, Charsets.UTF_8))
            sb.append('=')
            sb.append(URLEncoder.encode(value, Charsets.UTF_8))
        }

        return URI(baseUri.scheme, baseUri.authority, baseUri.path, sb.toString(), baseUri.fragment)
    }
}