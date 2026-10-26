package com.github.mh321Productions.handball4AllApi.api.data.endpoints

import com.github.mh321Productions.handball4AllApi.api.data.RawData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ClubData(val teams: List<ClubTeamInfo>)

data class ClubTeamInfo(
    val leagueId: Int, val name: String, val shortName: String,
    val ageGroup: AgeGroup, val refereeAllocation: RefereeAllocation, val refereeResponsibleOrg: Int?,
    val games: List<Game>
)

data class AgeGroup(val id: Int, val gender: Gender, val name: String, val year: Int, val solidSport: String)

@Serializable
enum class Gender {
    @SerialName("m") Male,
    @SerialName("w") Female,
    @SerialName("g") Mixed
}

@Serializable
data class RawClubData(val classes: List<RawClubTeamInfo>) : RawData<ClubData> {

    override fun getData() = ClubData(classes.map { it.getData() })
}

@Serializable
data class RawClubTeamInfo(
    val gClassID: String, val gClassSname: String, val gClassLname: String,
    val gClassAG: String, val gClassGender: Gender, val gClassAGsDesc: String,
    val gClassAGsYear: String, val gClassSolidsport: String, val gRefAllocType: RefereeAllocation,
    val gRefRespOrg: String, val games: List<RawGame>
) : RawData<ClubTeamInfo> {

    override fun getData(): ClubTeamInfo {
        val ageGroup = AgeGroup(gClassAG.toInt(), gClassGender, gClassAGsDesc, gClassAGsYear.toInt(), gClassSolidsport)
        return ClubTeamInfo(
            gClassID.toInt(), gClassLname, gClassSname,
            ageGroup, gRefAllocType, gRefRespOrg.toIntOrNull(),
            games.map { it.getData() }
        )
    }
}