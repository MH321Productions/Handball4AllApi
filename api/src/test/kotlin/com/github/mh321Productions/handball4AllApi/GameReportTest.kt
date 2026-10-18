package com.github.mh321Productions.handball4AllApi

import com.github.mh321Productions.handball4AllApi.api.Handball4All
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.IOException
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GameReportTest {

    @Test
    fun `Valid report for real data`() {
        val gameId = 3535431

        runBlocking {
            val result = assertDoesNotThrow { Handball4All.downloadGameReport(gameId) }

            assertTrue(result.isNotEmpty())
        }
    }

    @Test
    fun `Exception for wrong data`() {
        val gameId = 6519516 //WRONG

        runBlocking {
            assertFailsWith<IOException> { Handball4All.downloadGameReport(gameId) }
        }
    }
}