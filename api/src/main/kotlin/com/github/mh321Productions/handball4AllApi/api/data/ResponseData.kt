package com.github.mh321Productions.handball4AllApi.api.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNames

@Serializable
data class ResponseHeader(val version: String, @SerialName("(C)") val copyright: String)

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class MenuEntry(val list: Map<String, String>, @JsonNames("selectedID") val selected: String)

@Serializable
data class ResponseMenu(val org: MenuEntry, val period: MenuEntry, @SerialName("dt") val dates: MenuEntry)

@Serializable
data class ResponseDebug(val parseParams: Map<String, String>, val host: String)

@Serializable
data class H4AllResponse<THead, TContent>(val header: ResponseHeader, val menu: ResponseMenu, val head: THead, val content: TContent, val footer: List<JsonElement>, val error: JsonElement, val debug: ResponseDebug)