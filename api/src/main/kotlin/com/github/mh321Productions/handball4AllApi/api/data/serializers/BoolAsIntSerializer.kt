package com.github.mh321Productions.handball4AllApi.api.data.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object BoolAsIntSerializer: KSerializer<Boolean> {
    override val descriptor = PrimitiveSerialDescriptor("com.github.MH321Productions.Handball4AllApi.Boolean", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: Boolean) = encoder.encodeInt(if (value) 1 else 0)

    override fun deserialize(decoder: Decoder): Boolean {
        val number = decoder.decodeInt()
        return number != 0
    }
}