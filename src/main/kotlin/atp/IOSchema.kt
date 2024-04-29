package atp

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal object IOSchema: BaseSerializer<IO.Schema>(IO.Schema.serializer()) {

    override val validTypes = listOf(
        SchemaDefType.OBJECT,
        SchemaDefType.REF,
        SchemaDefType.UNION
    )

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            addDiscriminator(this)
        })
    }
}