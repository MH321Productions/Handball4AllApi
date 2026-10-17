package com.github.mh321Productions.handball4AllApi

import com.github.mh321Productions.handball4AllApi.api.Handball4All
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GymPlanTest {

    @Test
    fun `Valid response for real data`() {
        val org = 216 //BWHV
        val gymId = 865 //Hardtwaldhalle Sandhausen

        runBlocking {
            val result = Handball4All.getGymnasiumPlan(org, gymId)
            val response = assertDoesNotThrow { result.getOrThrow() }

            assertEquals("Hardtwaldhalle", response.head.name)
            assertEquals("HaWaSa", response.head.shortName)
        }
    }

    @Test
    fun `Invalid response for wrong data`() {
        val org = 216 //BWHV
        val gymId = 123456 //WRONG

        runBlocking {
            val result = Handball4All.getGymnasiumPlan(org, gymId)
            val response = assertDoesNotThrow { result.getOrThrow() }

            assertNull(response.head.name)
            assertNull(response.head.shortName)
        }
    }
}