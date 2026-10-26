package com.github.mh321Productions.handball4AllApi.calDavProvider

import com.github.mh321Productions.handball4AllApi.calDavProvider.data.CalendarCreationOverview
import com.github.mh321Productions.handball4AllApi.calDavProvider.data.CalendarInfo
import com.github.mh321Productions.handball4AllApi.calDavProvider.utils.setupClient
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.system.exitProcess

suspend fun main(args: Array<String>) {
    println("""
Handball4AllApi CalDAV Provider  Copyright (C) 2026  Mika Herrmann
This program comes with ABSOLUTELY NO WARRANTY.
This is free software, and you are welcome to redistribute it
under certain conditions.
""")

    if (args.isEmpty()) {
        println("No calendar info file provided")
        exitProcess(1)
    }

    val json = Json { prettyPrint = true; ignoreUnknownKeys = true; allowComments = true; allowTrailingComma = true }
    val raw = File(args[0]).readText()
    val calendarInfo = json.decodeFromString<CalendarCreationOverview>(raw)
    println("Calendar info: ${json.encodeToString(calendarInfo)}\n")

    val calendarData = calendarInfo.calendars.map { CalendarInfo.create(it) }

    print("\nConnecting to CalDav server ... ")
    val (client, account) = setupClient() ?: exitProcess(1)
    println("Done\n")


    calendarData.forEach {
        println("Processing calendar \"${it.name}\"")
        val calendar = account.findByName(it.name) ?: throw IllegalArgumentException("No calendar \"${it.name}\" found on the server, create it first")

        print("\tClearing old events ")
        client.fetchEvents(calendar.href).getOrThrow().forEach { event ->
            client.deleteEvent(client.buildEventUrl(calendar.href, event.event.uid), event.etag)
            print('.')
        }
        println(" Done")

        print("\tEntering new events ")
        it.games.forEach { game ->
            client.createEvent(calendar.href, game)
            print('.')
        }
        println(" Done\n")
    }

    println("Finishing up")
}