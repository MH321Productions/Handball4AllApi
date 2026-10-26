package com.github.mh321Productions.handball4AllApi.calDavProvider.utils

object Env {
    val serverUrl: String
        get() = getString("H4A_CAL_PROVIDER")

    val username: String
        get() = getString("H4A_CAL_USER")

    val password: String
        get() = getString("H4A_CAL_PASSWORD")

    val clubPrefix: String
        get() = getString("H4A_CLUB_PREFIX")

    private fun getString(name: String) = System.getenv(name) ?: throw NullPointerException("$name is not set")
}