package atp

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val description: String? = null,
    val name: String
)