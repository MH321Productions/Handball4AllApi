package com.github.mh321Productions.handball4AllApi

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonTest {

    fun stringToIntOrNull(primitive: JsonPrimitive): Int? = if (primitive.isString) primitive.content.toInt() else null

    @Test
    fun `Correct JSON Primitive conversion`()  {
        val i = JsonPrimitive(0)
        val s = JsonPrimitive("123456")

        assertEquals(null, stringToIntOrNull(i))
        assertEquals(123456, stringToIntOrNull(s))
    }
}