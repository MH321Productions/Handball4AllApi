package com.github.mh321Productions.handball4AllApi

import com.github.mh321Productions.handball4AllApi.api.Handball4All
import com.github.mh321Productions.handball4AllApi.api.data.exceptions.H4AllException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LeagueInfoTest {

    @Test
    fun `Valid response for real data`() {
        val org = 216 //BWHV
        val leagueId = 164116 //M-BL-RN

        runBlocking {
            val result = Handball4All.getLeagueInfo(org, leagueId)
            val league = assertDoesNotThrow { result.getOrThrow() }

            assertEquals("Männer Bezirksliga", league.head.name)
            assertEquals("M-BL-RN", league.head.shortName)
        }
    }

    @Test
    fun `Error response for wrong data`() {
        val org = 219 //WRONG ORG
        val leagueId = 164116 //M-BL-RN

        runBlocking {
            val result = Handball4All.getLeagueInfo(org, leagueId)
            assertTrue(result.isFailure)

            val exception = result.exceptionOrNull()
            assertTrue(exception is H4AllException)
        }
    }
}
