package com.github.mh321Productions.handball4AllApi.calDavProvider.utils

import org.onekash.icaldav.client.CalDavClient
import org.onekash.icaldav.model.CalDavAccount
import org.onekash.icaldav.model.DavResult

suspend fun setupClient(): Pair<CalDavClient, CalDavAccount>? {
    val client = CalDavClient.forProvider(Env.serverUrl, Env.username, Env.password)
    when (val discovery = client.discoverAccount(Env.serverUrl)) {
        is DavResult.Success -> return Pair(client, discovery.value)

        is DavResult.HttpError -> {
            println("Couldn't access server ${Env.serverUrl}: HTTP ${discovery.code}, ${discovery.message}")
            return null
        }

        is DavResult.NetworkError -> {
            println("Couldn't access server ${Env.serverUrl}: Network error ${discovery.exception.message}")
            return null
        }

        is DavResult.ParseError -> {
            println("Couldn't access server ${Env.serverUrl}: Parse error ${discovery.message}")
            return null
        }
    }
}