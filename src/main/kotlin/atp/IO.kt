package atp

import kotlinx.serialization.Serializable

@Serializable
data class IO(
    val description: String? = null,
    val encoding: String,
    @Serializable(with = InputSchemaSerializer::class) val schema: Schema? = null
) {
    @Serializable
    sealed interface Schema
}