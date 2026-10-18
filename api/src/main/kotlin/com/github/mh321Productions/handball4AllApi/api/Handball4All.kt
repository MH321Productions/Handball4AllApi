package com.github.mh321Productions.handball4AllApi.api

import com.github.mh321Productions.handball4AllApi.api.data.H4AllResponse
import com.github.mh321Productions.handball4AllApi.api.data.RawH4AllResponse
import com.github.mh321Productions.handball4AllApi.api.data.RawData
import com.github.mh321Productions.handball4AllApi.api.data.RawResponseMenu
import com.github.mh321Productions.handball4AllApi.api.data.endpoints.*
import com.github.mh321Productions.handball4AllApi.api.data.exceptions.GameNotLiveException
import com.github.mh321Productions.handball4AllApi.api.data.exceptions.H4AllException
import com.github.mh321Productions.handball4AllApi.api.data.exceptions.NoGameReportException
import kotlinx.coroutines.future.await
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecodingException
import kotlinx.serialization.json.JsonEncodingException
import java.io.IOException
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.jvm.optionals.getOrDefault

/**
 * The base class for the Handball4all API
 */
object Handball4All {
    private const val BASE_URL = "https://spo.handball4all.de/service/if_g_json.php"
    private const val REPORT_BASE_URL = "https://spo.handball4all.de/misc/sboPublicReports.php"
    private const val LIVE_BASE_URL = "https://spo.handball4all.de/service/ticker/index.html"
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }
    private val client = HttpClient.newHttpClient()


    /**
     * Queries the information about a league,
     * containing the games, the scores and the ranking.
     *
     * This query allows to show all games during the season.
     *
     * @param organization The organization hosting the league (The `ogId` parameter in the Browser url or [RawResponseMenu.org], e.g. `216` for the BWHV)
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
     * @param organization The organization hosting the league (The `ogId` parameter in the Browser url or [RawResponseMenu.org], e.g. `216` for the BWHV)
     * @param leagueId The league id (The `lId` parameter in the Browser url or [GameList.leagueId])
     * @param date The week to query (the week's monday as `YYYY-MM-DD`, see [RawResponseMenu.dates] for possible values), or `null` for the current week
     */
    suspend fun getLeagueInfo(organization: Int, leagueId: Int, date: String?) = getLeagueInfo(organization, leagueId, null, date, false)

    /**
     * Queries the information about a league,
     * containing the games, the scores and the ranking.
     *
     * This query shows all games for a specified team under [LeagueInfoContent.futureGames].
     *
     * @param organization The organization hosting the league (The `ogId` parameter in the Browser url or [RawResponseMenu.org], e.g. `216` for the BWHV)
     * @param leagueId The league id (The `lId` parameter in the Browser url or [GameList.leagueId])
     * @param teamId The team id (The `tId` parameter in the Browser url or [TeamInfo.teamId])
     */
    suspend fun getLeagueInfo(organization: Int, leagueId: Int, teamId: Int) = getLeagueInfo(organization, leagueId, teamId, null, false)

    /**
     * Queries information about a gymnasium (name, address, wax regulations, accessibility information, etc.)
     *
     * @param organization The organization responsible for the gymnasium (The `ogId` parameter in the Browser url or [RawResponseMenu.org], e.g. `216` for the BWHV)
     * @param gymId The gymnasium id (The `gId` parameter in the Browser or [Gymnasium.id])
     */
    @OptIn(ExperimentalSerializationApi::class)
    suspend fun getGymnasiumInfo(organization: Int, gymId: Int): Result<H4AllGymnasiumResponse> {
        val query = mapOf(
            "cmd" to "gi",
            "og" to organization.toString(),
            "g" to gymId.toString()
        )
        val request = HttpRequest.newBuilder(buildUri(query)).GET().build()
        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).await()

        try {
            val rawData = json.decodeFromString<List<H4AllRawGymnasiumResponse>>(response.body()).first()
            rawData.gymnasiumInfo.queryOrg = organization
            rawData.gymnasiumInfo.queryGymnasium = gymId
            return Result.success(rawData.getData())
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

    /**
     * Queries the game plan of a gymnasium
     *
     * @param organization The organization responsible for the gymnasium (The `ogId` parameter in the Browser url or [RawResponseMenu.org], e.g. `216` for the BWHV)
     * @param gymId The gymnasium id (The `gId` parameter in the Browser or [Gymnasium.id])
     * @param date The week to query (the week's monday as `YYYY-MM-DD`, see [RawResponseMenu.dates] for possible values), or `null` for the current week
     */
    suspend fun getGymnasiumPlan(organization: Int, gymId: Int, date: String? = null) = runRequest<GymPlanContent, RawGymPlanContent>(buildMap {
        put("cmd", "pgy")
        put("og", organization.toString())
        put("g", gymId.toString())
        if (date != null) { put("do", date) }
    })

    /**
     * Queries the game plan of a club
     *
     * @param organization The organization the club belongs to (The `ogId` parameter in the Browser url or [RawResponseMenu.org], e.g. `216` for the BWHV)
     * @param clubId The club id (The `cId` parameter in the browser)
     * @param date The week to query (the week's monday as `YYYY-MM-DD`, see [RawResponseMenu.dates] for possible values), or `null` for the current week
     */
    suspend fun getClubPlan(organization: Int, clubId: Int, date: String? = null) = runRequest<ClubData, RawClubData>(buildMap {
        put("cmd", "pcu")
        put("og", organization.toString())
        put("c", clubId.toString())
        if (date != null) { put("do", date) }
    })

    /**
     * Download the game report (if one exists)
     *
     * @param gameId The game's GID (see [Game.gameId])
     * @return The report PDF as a Byte array
     * @throws IOException When the response is empty, not a PDF or the request failed
     */
    suspend fun downloadGameReport(gameId: Int): ByteArray {
        val query = mapOf("sGID" to gameId.toString())
        val request = HttpRequest.newBuilder(buildUri(query, REPORT_BASE_URL)).GET().build()
        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray()).await()

        val contentType = response.headers().firstValue("content-type").getOrDefault("")

        if (contentType != "application/pdf") throw IOException("The report response is not a PDF")
        if (response.body().isEmpty()) throw IOException("The report response is empty")

        return response.body()
    }

    /**
     * Download the game report (if one exists)
     *
     * @param game The game instance
     * @return The report PDF as a Byte array
     * @throws NoGameReportException When the game doesn't have a report (yet)
     * @throws IOException When the response is empty, not a PDF or the request failed
     */
    suspend fun downloadGameReport(game: Game) = downloadGameReport(game.gameId ?: throw NoGameReportException(game))

    /**
     * Construct the URL for accessing the live ticker of a game
     *
     * @param game The game instance
     * @return The ticker url as a [URL]
     * @throws GameNotLiveException If the game is not live (yet)
     */
    fun getLiveTickerUrl(game: Game): URL = buildUri(mapOf("token" to game.token.ifBlank { throw GameNotLiveException(game) })).toURL()

    private suspend fun getLeagueInfo(organization: Int, leagueId: Int, teamId: Int? = null, date: String? = null, showAllGames: Boolean = false)
    = runRequest<LeagueInfoContent, RawLeagueInfoContent>(
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
    private suspend inline fun <TContent, reified TRawContent : RawData<TContent>> runRequest(query: Map<String, String>): Result<H4AllResponse<TContent>> {
        val request = HttpRequest.newBuilder(buildUri(query)).GET().build()
        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).await()

        try {
            val rawData = json.decodeFromString<List<RawH4AllResponse<TRawContent>>>(response.body()).first()
            val highLevelData = H4AllResponse(rawData.header, rawData.menu.getData(), rawData.head, rawData.content.getData(), rawData.footer, rawData.error, rawData.debug)
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

    private fun buildUri(query: Map<String, String>, baseUrl: String = BASE_URL): URI {
        val baseUri = URI(baseUrl)

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