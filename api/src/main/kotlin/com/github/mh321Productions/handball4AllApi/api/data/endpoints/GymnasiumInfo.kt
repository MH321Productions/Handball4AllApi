package com.github.mh321Productions.handball4AllApi.api.data.endpoints

import com.github.mh321Productions.handball4AllApi.api.data.RawData
import com.github.mh321Productions.handball4AllApi.api.data.ResponseDebug
import com.github.mh321Productions.handball4AllApi.api.data.ResponseHeader
import com.github.mh321Productions.handball4AllApi.api.data.exceptions.GymNotFoundException
import com.github.mh321Productions.handball4AllApi.api.data.serializers.BoolAsIntSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement

data class H4AllGymnasiumResponse(val header: ResponseHeader, val gymnasiumInfo: GymnasiumInfo, val footer: List<JsonElement>, val error: JsonElement, val debug: ResponseDebug)

@Serializable
data class H4AllRawGymnasiumResponse(val header: ResponseHeader, val gymnasiumInfo: RawGymnasiumInfo, val footer: List<JsonElement>, val error: JsonElement, val debug: ResponseDebug) : RawData<H4AllGymnasiumResponse> {

    override fun getData() = H4AllGymnasiumResponse(header, gymnasiumInfo.getData(), footer, error, debug)
}

data class GymnasiumInfo(
    val name: String, val number: Int, val org: String,
    val street: String, val town: String, val phone: String,
    val permitOrg1: String, val permitOrg2: String, val wax: String,
    val geoLon: Double, val geoLat: Double, val accessibility: AccessibilityList
)

@Serializable
data class AccessibilityList(
    @SerialName("btentoilette") val accessibleRestroom: AccessibilityEntry,
    @SerialName("zugangspielfeld") val accessToField: AccessibilityEntry,
    @SerialName("zugangverpflegung") val accessToFood: AccessibilityEntry,
    @SerialName("zugangtribuene") val accessToSpectatorStand: AccessibilityEntry,
    @SerialName("bdusche") val accessibleShowers: AccessibilityEntry,
    @SerialName("rollstuhlparken") val wheelchairParkingSpot: AccessibilityEntry,
    @SerialName("oeverkehr") val publicTransit: AccessibilityEntry,
    @SerialName("bumkleide") val accessibleChangingRoom: AccessibilityEntry,
    @SerialName("zuganghalle") val accessToGymnasium: AccessibilityEntry,
    @SerialName("bparken") val accessibleParking: AccessibilityEntry,
    @SerialName("zugangumkleide") val accessToChangingRooms: AccessibilityEntry,
)

@Serializable
data class AccessibilityEntry(@Serializable(BoolAsIntSerializer::class) val available: Boolean, val details: String?)

@Serializable
data class RawGymnasiumInfo(
    val name: String?, @SerialName("no") val number: String?, val org: String?,
    val street: String?, val town: String?, val phone: String?,
    @SerialName("permit_org_1") val permitOrg1: String, @SerialName("permit_org_2") val permitOrg2: String, val wax: String,
    @SerialName("geolon") val geoLon: String, @SerialName("geolat") val geoLat: String, val barrierefreiheit: AccessibilityList? = null
) : RawData<GymnasiumInfo> {

    @Transient
    var queryOrg: Int = 0
    @Transient
    var queryGymnasium: Int = 0

    private val ex: GymNotFoundException
        get() = GymNotFoundException(queryOrg, queryGymnasium)

    override fun getData() = GymnasiumInfo(
        name ?: throw ex, number?.toInt() ?: throw ex, org ?: throw ex,
        street ?: throw ex, town ?: throw ex, phone ?: throw ex,
        permitOrg1, permitOrg2, wax,
        geoLon.toDoubleOrNull() ?: throw ex, geoLat.toDoubleOrNull() ?: throw ex, barrierefreiheit ?: throw ex,
    )
}
