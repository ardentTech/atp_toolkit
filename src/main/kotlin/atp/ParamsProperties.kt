package atp

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal object ParamsProperties: BaseSerializer<Map<String, SchemaDef>>(
    MapSerializer(String.serializer(), SchemaDef.serializer())
) {

    override val validTypes = listOf(
        SchemaDefType.ARRAY,
        SchemaDefType.BOOLEAN,
        SchemaDefType.INTEGER,
        SchemaDefType.STRING,
        SchemaDefType.UNKNOWN
    )

    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                addDiscriminator(this)
            })
        }
        return JsonObject(content = content)
    }
}