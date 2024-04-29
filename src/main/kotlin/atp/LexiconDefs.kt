package atp

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

internal object LexiconDefs: BaseSerializer<Map<String, SchemaDef.PrimaryType>>(
    MapSerializer(String.serializer(), SchemaDef.PrimaryType.serializer())
) {

    override val validTypes = listOf(
        SchemaDefType.OBJECT,
        SchemaDefType.PROCEDURE,
        SchemaDefType.QUERY,
        SchemaDefType.RECORD,
        SchemaDefType.SUBSCRIPTION
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