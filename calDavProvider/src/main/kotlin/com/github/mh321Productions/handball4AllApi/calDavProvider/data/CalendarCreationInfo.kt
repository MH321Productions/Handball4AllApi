package com.github.mh321Productions.handball4AllApi.calDavProvider.data

import kotlinx.serialization.Serializable

@Serializable
data class CalendarCreationOverview(val calendars: List<CalendarCreationInfo>)

@Serializable
data class CalendarCreationInfo(val name: String, val teams: List<CalendarTeam>)

@Serializable
data class CalendarTeam(val orgId: Int, val leagueId: Int, val teamId: Int, val prefix: String = "", val suffix: String = "")