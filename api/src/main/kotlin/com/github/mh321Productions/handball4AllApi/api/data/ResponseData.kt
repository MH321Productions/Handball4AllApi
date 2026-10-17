package com.github.mh321Productions.handball4AllApi.api.data

import com.github.mh321Productions.handball4AllApi.api.data.serializers.BoolAsIntSerializer
import com.github.mh321Productions.handball4AllApi.api.data.utils.string
import com.github.mh321Productions.handball4AllApi.api.data.utils.stringOrNull
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNames
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class ResponseHeader(val version: String, @SerialName("(C)") val copyright: String)

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class RawMenuEntry(val list: JsonElement, @JsonNames("selectedID") val selected: JsonPrimitive) : RawData<MenuEntry> {

    override fun getData(): MenuEntry{
        val decodedList = if (list is JsonObject) list.mapValues { (_, v) -> v.jsonPrimitive.string } else mapOf()

        return MenuEntry(decodedList, selected.stringOrNull)
    }
}

@Serializable
data class RawResponseMenu(val org: JsonElement, val period: JsonElement, @SerialName("dt") val dates: JsonElement) : RawData<ResponseMenu> {

    override fun getData(): ResponseMenu {
        val rawOrg = if (org is JsonObject) Json.decodeFromJsonElement(RawMenuEntry.serializer(), org) else null
        val rawPeriod  = if (period is JsonObject) Json.decodeFromJsonElement(RawMenuEntry.serializer(), period) else null
        val rawDates = if (dates is JsonObject) Json.decodeFromJsonElement(RawMenuEntry.serializer(), dates) else null

        return ResponseMenu(rawOrg?.getData(), rawPeriod?.getData(), rawDates?.getData())
    }
}

data class MenuEntry(val list: Map<String, String>, val selected: String?)

data class ResponseMenu(val org: MenuEntry?, val period: MenuEntry?, val dates: MenuEntry?)

@Serializable
data class ResponseHead(
    val name: String?, @SerialName("sname") val shortName: String?,
    val headline1: String, val headline2: String,
    val actualized: String, @Serializable(BoolAsIntSerializer::class) val live: Boolean,
    @SerialName("repURL") val reportUrl: String, val scoreShowDataPerGame: Boolean = false
)

@Serializable
data class ResponseDebug(val parseParams: Map<String, String>, val host: String)

@Serializable
data class RawH4AllResponse<TContent>(val header: ResponseHeader, val menu: RawResponseMenu, val head: ResponseHead, val content: TContent, val footer: List<JsonElement>, val error: JsonElement, val debug: ResponseDebug)

data class H4AllResponse<TContent>(val header: ResponseHeader, val menu: ResponseMenu, val head: ResponseHead, val content: TContent, val footer: List<JsonElement>, val error: JsonElement, val debug: ResponseDebug)