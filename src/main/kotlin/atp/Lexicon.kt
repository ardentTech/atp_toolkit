package atp

import kotlinx.serialization.Serializable

@Serializable
data class Lexicon(
    @Serializable(with = LexiconDefsSerializer::class) val defs: Map<String, SchemaDef.PrimaryType>,
    val description: String? = null,
    val id: String,
    val lexicon: Int,
    val revision: Int? = null
) {
    init {
        require(defs.isNotEmpty())

        // allow multiple `object` defs but only one primary def
        var count = 0
        defs.forEach {
            if (it.value !is SchemaDef.Object) {
                count++
                if (count > 1) throw IllegalArgumentException()
            }
        }
    }
}