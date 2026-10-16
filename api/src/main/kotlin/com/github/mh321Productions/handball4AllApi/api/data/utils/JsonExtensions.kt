package com.github.mh321Productions.handball4AllApi.api.data.utils

import kotlinx.serialization.json.JsonPrimitive

val JsonPrimitive.string: String
    get() = if (isString) this.content else error("Element $content is not a String")

val JsonPrimitive.stringOrNull: String?
    get() = if (isString) this.content else null

val JsonPrimitive.stringToInt: Int
    get() = string.toInt()

val JsonPrimitive.stringToIntOrNull: Int?
    get() = stringOrNull?.toIntOrNull()