package com.github.mh321Productions.handball4AllApi.calDavProvider.utils

import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

suspend fun randomDelay() {
    val random = Random(System.currentTimeMillis())
    val time = random.nextInt(11, 997)
    delay(time.milliseconds)
}

fun LocalDateTime.toZonedDateTime(zoneId: ZoneId = ZoneId.systemDefault()): ZonedDateTime = ZonedDateTime.of(this.toJavaLocalDateTime(), zoneId)

fun min(a: Duration, b: Duration) = if (a < b) a else b