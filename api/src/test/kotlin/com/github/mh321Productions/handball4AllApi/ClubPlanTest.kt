package com.github.mh321Productions.handball4AllApi

import com.github.mh321Productions.handball4AllApi.api.Handball4All
import com.github.mh321Productions.handball4AllApi.api.data.endpoints.Gender
import com.github.mh321Productions.handball4AllApi.api.data.exceptions.H4AllException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ClubPlanTest {

    @Test
    fun `Valid response for real data`() {
        val org = 216 //BWHV
        val clubId = 7991 //HSG Hardtwald

        runBlocking {
            val result = Handball4All.getClubPlan(org, clubId)
            val response = assertDoesNotThrow { result.getOrThrow() }

            assertTrue(response.content.teams.all { it.ageGroup.gender != Gender.Female }) //The HSG only has male teams (some in mixed leagues)
        }
    }

    @Test
    fun `Failed result for wrong data`() {
        val org = 216 //BWHV
        val clubId = 79910 //WRONG

        runBlocking {
            val result = Handball4All.getClubPlan(org, clubId)
            val err = assertFailsWith<H4AllException> { result.getOrThrow() }

            assertEquals(-1, err.status)
        }
    }
}