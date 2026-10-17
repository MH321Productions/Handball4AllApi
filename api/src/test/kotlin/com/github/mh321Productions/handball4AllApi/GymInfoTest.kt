package com.github.mh321Productions.handball4AllApi

import com.github.mh321Productions.handball4AllApi.api.Handball4All
import com.github.mh321Productions.handball4AllApi.api.data.exceptions.GymNotFoundException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GymInfoTest {

    @Test
    fun `Valid response for real data`() {
        val org = 216 //BWHV
        val gymId = 865 //Hardtwaldhalle Sandhausen

        runBlocking {
            val result = Handball4All.getGymnasiumInfo(org, gymId)
            val gym = assertDoesNotThrow { result.getOrThrow() }

            assertEquals(22004, gym.gymnasiumInfo.number)
            assertEquals("Hardtwaldhalle", gym.gymnasiumInfo.name)
            assertTrue(gym.gymnasiumInfo.accessibility.publicTransit.available)
        }
    }

    @Test
    fun `Error response for wrong data`() {
        val org = 216 //BWHV
        val gymId = 123 //WRONG

        runBlocking {
            val result = Handball4All.getGymnasiumInfo(org, gymId)
            val err = assertFailsWith<GymNotFoundException> { result.getOrThrow() }
            assertEquals(org, err.orgId)
            assertEquals(gymId, err.gymId)
        }
    }
}