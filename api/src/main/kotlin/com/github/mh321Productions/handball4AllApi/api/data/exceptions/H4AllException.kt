package com.github.mh321Productions.handball4AllApi.api.data.exceptions

import kotlinx.serialization.Serializable

@Serializable
data class H4AllException(val status: Int, val statusText: String, val hostname: String) : Exception("The host $hostname couldn't process request ($status): $statusText")