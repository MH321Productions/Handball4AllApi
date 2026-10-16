package com.github.mh321Productions.handball4AllApi.api.data.endpoints

import com.github.mh321Productions.handball4AllApi.api.data.RawData
import com.github.mh321Productions.handball4AllApi.api.data.serializers.BoolAsIntSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull

@Serializable
data class LeagueInfoHead(
    val name: String, @SerialName("sname") val shortName: String,
    val headline1: String, val headline2: String,
    val actualized: String, @Serializable(BoolAsIntSerializer::class) val live: Boolean,
    @SerialName("repURL") val reportUrl: String, val scoreShowDataPerGame: Boolean
) : RawData<LeagueInfoHead> {
    override fun getData() = this
}

data class LeagueInfoContent(
    val actualGames: GameList, val futureGames: GameList,
    val score: List<LeagueScore>, val scoreComments: List<String>, val scoreInfos: List<String>,
    val teamsList: List<TeamInfo>, val scoreShowDataPerGame: Boolean
)


data class LeagueScore(
    val tabScore: Int?, val teamId: Int, val teamName: String, val live: Boolean,
    val playedGames: Int, val wonGames: Int, val equalGames: Int, val lostGames: Int,
    val goalsShot: Int, val goalsGot: Int, val pointsPlus: Int, val pointsMinus: Int,
    val pointsPerGame10: String, val goalsDiffPerGame: String, val goalsShotPerGame: String,
    val posCriterion: String
)

data class TeamInfo(val sort: Int, val teamId: Int, val teamName: String)

@Serializable
data class RawTeamInfo(val sort: String, @SerialName("teamID") val teamId: String, val teamName: String) : RawData<TeamInfo> {

    override fun getData() = TeamInfo(sort.toInt(), teamId.toInt(), teamName)
}

@Serializable
data class RawLeagueScore(
    val tabScore: JsonPrimitive, val tabTeamID: String, val tabTeamname: String, val liveTeam: Boolean,
    val numPlayedGames: Int, val numWonGames: Int, val numEqualGames: Int, val numLostGames: Int,
    val numGoalsShot: Int, val numGoalsGot: Int, val pointsPlus: Int, val pointsMinus: Int,
    val pointsPerGame10: String, val numGoalsDiffperGame: String, val numGoalsShotperGame: String,
    val posCriterion: String
) : RawData<LeagueScore> {
    override fun getData() = LeagueScore(
        tabScore.intOrNull, tabTeamID.toInt(), tabTeamname, liveTeam,
        numPlayedGames, numWonGames, numEqualGames, numLostGames,
        numGoalsShot, numGoalsGot, pointsPlus, pointsMinus,
        pointsPerGame10, numGoalsDiffperGame, numGoalsShotperGame,
        posCriterion
    )
}

@Serializable
data class RawLeagueInfoContent(
    val actualGames: RawGameList, val futureGames: RawGameList,
    val score: List<RawLeagueScore>, val scoreComments: List<String>, val scoreInfos: List<JsonPrimitive>,
    val teamsList: List<RawTeamInfo>, val scoreShowDataPerGame: Boolean
) : RawData<LeagueInfoContent> {
    override fun getData() = LeagueInfoContent(
        actualGames.getData(), futureGames.getData(),
        score.map { it.getData() }, scoreComments, scoreInfos.map { it.content },
        teamsList.map { it.getData() }, scoreShowDataPerGame
    )
}
