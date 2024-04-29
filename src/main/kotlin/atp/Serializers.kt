package atp

import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

internal object LexiconDefsSerializer: BaseSerializer<Map<String, SchemaDef.PrimaryType>>(
    MapSerializer(String.serializer(), SchemaDef.PrimaryType.serializer())
) {

    // TODO unit test
    override val validTypes = listOf(
        LexiconType.OBJECT,
        LexiconType.PROCEDURE,
        LexiconType.QUERY,
        LexiconType.RECORD,
        LexiconType.SUBSCRIPTION
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

internal object InputSchemaSerializer: BaseSerializer<IO.Schema>(IO.Schema.serializer()) {

    // TODO unit test
    override val validTypes = listOf(
        LexiconType.OBJECT,
        LexiconType.REF,
        LexiconType.UNION
    )

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            addDiscriminator(this)
        })
    }
}

internal object ParamsPropertiesSerializer: BaseSerializer<Map<String, SchemaDef>>(MapSerializer(String.serializer(), SchemaDef.serializer())) {

    // TODO unit test
    override val validTypes = listOf(
        LexiconType.ARRAY,
        LexiconType.BOOLEAN,
        LexiconType.INTEGER,
        LexiconType.STRING,
        LexiconType.UNKNOWN
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

internal object ObjectPropertiesSerializer: BaseSerializer<Map<String, SchemaDef>>(MapSerializer(String.serializer(), SchemaDef.serializer())) {
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

internal object FieldTypeSerializer: BaseSerializer<SchemaDef.FieldType>(SchemaDef.FieldType.serializer()){
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            addDiscriminator(this)
        })
    }
}