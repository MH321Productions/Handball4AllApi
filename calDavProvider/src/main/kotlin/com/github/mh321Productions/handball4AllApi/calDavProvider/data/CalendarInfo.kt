package com.github.mh321Productions.handball4AllApi.calDavProvider.data

import com.github.mh321Productions.handball4AllApi.api.Handball4All
import com.github.mh321Productions.handball4AllApi.calDavProvider.utils.Env
import com.github.mh321Productions.handball4AllApi.calDavProvider.utils.min
import com.github.mh321Productions.handball4AllApi.calDavProvider.utils.randomDelay
import com.github.mh321Productions.handball4AllApi.calDavProvider.utils.toICalEvent
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.onekash.icaldav.model.ICalEvent
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

data class CalendarInfo(val name: String, val games: List<ICalEvent>) {
    companion object {
        suspend fun create(info: CalendarCreationInfo): CalendarInfo {
            println("Querying ${info.teams.size} team(s) for the \"${info.name}\" calendar")
            val games = info.teams
                .flatMap { team ->
                    println("\tQuerying $team")
                    val list = Handball4All.getLeagueInfo(team.orgId, team.leagueId, team.teamId).getOrThrow().content.futureGames.games
                    randomDelay()
                    list.zip(List(list.size) { team })
                }
                .filter { it.first.homeTeam.startsWith(Env.clubPrefix) || it.first.guestTeam.startsWith(Env.clubPrefix) }
                .sortedBy { it.first.date }

            val durations = games
                .groupBy {it.first.date.date}
                .flatMap {
                    val d = mutableListOf<Duration>()
                    if (it.value.size > 1) {
                        for (i in 0..(it.value.size - 2)) {
                            val a = it.value[i].first.date.toInstant(TimeZone.currentSystemDefault())
                            val b = it.value[i + 1].first.date.toInstant(TimeZone.currentSystemDefault())
                            d.add(min(b - a, 1.hours))
                        }
                    }
                    d.add(1.hours)
                    d
                }

            val events = games.mapIndexed { i, (game, team) -> game.toICalEvent(durations[i], team.prefix, team.suffix) }

            return CalendarInfo(info.name, events)
        }
    }
}
