package com.github.mh321Productions.handball4AllApi.api.data.endpoints

import com.github.mh321Productions.handball4AllApi.api.data.RawData
import com.github.mh321Productions.handball4AllApi.api.data.utils.stringToIntOrNull
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

@Serializable
enum class GameRoboTextState {
    @SerialName("unseen") Unseen,
    @SerialName("generated") Generated
}

@Serializable
enum class RefereeAllocation {
    @SerialName("not_defined") NotDefined,
    @SerialName("org_assigned") OrgAssigned
}

data class Gymnasium(val id: String, val number: String, val name: String, val postal: String, val town: String, val street: String)

data class Game(
    val id: Int, val gameId: Int?, val publicNumber: Int, val live: Boolean,
    val token: String, val appId: String,
    val date: LocalDateTime, val gymnasium: Gymnasium,
    val homeTeam: String, val guestTeam: String,
    val homeGoals: Int?, val guestGoals: Int?, val homeGoals1: Int?, val guestGoals1: Int?, val homePoints: Int?, val guestPoints: Int?,
    val comment: String, val groupSortText: String, val referee: String, val roboTextState: GameRoboTextState
)

data class GameList(
    val leagueId: Int, val leagueShortName: String, val leagueLongName: String?,
    val leagueAG: Int?, val leagueSolidSport: Int?,
    val refereeAllocation: RefereeAllocation, val refereeResponsibleOrg: Int?,
    val games: List<Game>
)

@Serializable
data class RawGameList(
    val gClassID: String, val gClassSname: String, val gClassLname: String?,
    val gClassAG: String?, val gClassSolidsport: String?,
    val gRefAllocType: RefereeAllocation, val gRefRespOrg: JsonPrimitive,
    val games: List<RawGame>
) : RawData<GameList> {

    override fun getData() = GameList(
        gClassID.toInt(), gClassSname, gClassLname,
        gClassAG?.toInt(), gClassSolidsport?.toInt(),
        gRefAllocType, gRefRespOrg.stringToIntOrNull,
        games.map { it.getData() }
    )
}


@Serializable
data class RawGame(
    val gID: String, val sGID: JsonPrimitive, val gNo: String, val live: Boolean,
    val gToken: String, val gAppid: String,
    val gDate: String, val gWDay: String, val gTime: String,
    val gGymnasiumID: String, val gGymnasiumNo: String, val gGymnasiumName: String, val gGymnasiumPostal: String, val gGymnasiumTown: String, val gGymnasiumStreet: String,
    val gHomeTeam: String, val gGuestTeam: String,
    val gHomeGoals: String, val gGuestGoals: String,
    @SerialName("gHomeGoals_1") val gHomeGoals1: String, @SerialName("gGuestGoals_1") val gGuestGoals1: String,
    val gHomePoints: String, val gGuestPoints: String,
    val gComment: String, val gGroupsortTxt: String, val gReferee: String, val robotextstate: GameRoboTextState
) : RawData<Game> {
    override fun getData(): Game {
        val gym = Gymnasium(gGymnasiumID, gGymnasiumNo, gGymnasiumName, gGymnasiumPostal, gGymnasiumTown, gGymnasiumStreet)

        val dateSplit = gDate.split(".") //The date format is DD.MM.YY
        val day = dateSplit[0].toInt()
        val month = dateSplit[1].toInt()
        val year = dateSplit[2].toInt() + 2000

        val timeSplit = gTime.split(":")
        val hours = timeSplit[0].toInt()
        val minutes = timeSplit[1].toInt()

        val date = LocalDateTime(year, month, day, hours, minutes, 0, 0)

        return Game(
            gID.toInt(), sGID.stringToIntOrNull, gNo.toInt(), live, gToken, gAppid,
            date, gym,
            gHomeTeam, gGuestTeam,
            gHomeGoals.toIntOrNull(), gGuestGoals.toIntOrNull(),
            gHomeGoals1.toIntOrNull(), gGuestGoals1.toIntOrNull(),
            gHomePoints.toIntOrNull(), gGuestPoints.toIntOrNull(),
            gComment, gGroupsortTxt, gReferee, robotextstate
        )
    }
}
