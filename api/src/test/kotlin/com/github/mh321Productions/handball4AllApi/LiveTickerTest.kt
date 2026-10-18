package com.github.mh321Productions.handball4AllApi

import com.github.mh321Productions.handball4AllApi.api.Handball4All
import com.github.mh321Productions.handball4AllApi.api.data.endpoints.Game
import com.github.mh321Productions.handball4AllApi.api.data.endpoints.GameRoboTextState
import com.github.mh321Productions.handball4AllApi.api.data.endpoints.Gymnasium
import com.github.mh321Productions.handball4AllApi.api.data.exceptions.GameNotLiveException
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Clock

class LiveTickerTest {

    private fun getMockGame(token: String) = Game(
        1, null, 1, token.isNotEmpty(), token, "",
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        Gymnasium(1, 1, "Test", "12345", "Test", "Test"),
        "Home", "Guest", null, null,
        null, null, null, null,
        "", "", "", GameRoboTextState.Unseen
    )

    @Test
    fun `Valid URL for live game`() {
        val game = getMockGame("abc")
        val url = assertDoesNotThrow { Handball4All.getLiveTickerUrl(game) }
        assertTrue(url.toString().endsWith("?token=abc"))
    }

    @Test
    fun `Exception for non-live game`() {
        val game = getMockGame("")
        assertFailsWith<GameNotLiveException> { Handball4All.getLiveTickerUrl(game) }
    }
}