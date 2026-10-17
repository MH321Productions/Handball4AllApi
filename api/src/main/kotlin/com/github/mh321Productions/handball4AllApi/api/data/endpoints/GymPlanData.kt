package com.github.mh321Productions.handball4AllApi.api.data.endpoints

import com.github.mh321Productions.handball4AllApi.api.data.RawData
import kotlinx.serialization.Serializable

data class GymPlanContent(val gameList: List<GameList>)

@Serializable
data class RawGymPlanContent(val gList: List<RawGameList>) : RawData<GymPlanContent> {

    override fun getData() = GymPlanContent(gList.map { it.getData() })
}