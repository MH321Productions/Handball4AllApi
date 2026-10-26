package com.github.mh321Productions.handball4AllApi.calDavProvider.utils

import com.github.mh321Productions.handball4AllApi.api.data.endpoints.Game
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.onekash.icaldav.model.*
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaInstant
import kotlin.uuid.Uuid

fun Game.toICalEvent(duration: Duration = 1.hours, prefix: String, suffix: String): ICalEvent {
    val uid = Uuid.random().toString()
    val start = ICalDateTime.fromZonedDateTime(date.toZonedDateTime())
    val endRaw = date.toInstant(TimeZone.currentSystemDefault()) + duration
    val end = ICalDateTime.fromZonedDateTime(ZonedDateTime.ofInstant(endRaw.toJavaInstant(), ZoneId.systemDefault()))
    val title = "$prefix$homeTeam - $guestTeam$suffix"
    val location = gymnasium?.let {
        ICalLocation(
            Uuid.random().toString(),
            it.name,
            null,
            null,
            listOf(LocationType.INDOOR),
            null,
            StructuredAddress(
                streetAddress = it.street,
                locality = it.town,
                postalCode = it.postal
            )
        )
    }
    val locShort = gymnasium?.let { "${it.name}, ${it.street}, ${it.postal} ${it.town}" }

    val description = StringBuilder()
    if (homeGoals != null && guestGoals != null) {
        description.append("Result: $homeGoals:$guestGoals")
        if (homeGoals1 != null && guestGoals1 != null) description.append(" ($homeGoals1:$guestGoals1)")
        description.append('\n')
    }
    if (referee.isNotBlank()) description.append("Referee: $referee\n")
    if (comment.isNotBlank()) description.append("Comment: $comment\n")

    return ICalEvent(
        uid,
        ICalEvent.generateImportId(uid, null),
        title,
        description.toString(),
        locShort,
        start,
        end,
        null,
        false,
        EventStatus.CONFIRMED,
        0,
        null,
        emptyList(),
        emptyList(),
        null,
        null,
        emptyList(),
        emptyList(),
        null,
        emptyList(),
        null,
        null,
        null,
        null,
        Transparency.OPAQUE,
        null,
        locations = if (location != null) listOf(location) else emptyList(),
        rawProperties = emptyMap()
    )
}