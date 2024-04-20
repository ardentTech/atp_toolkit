import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import kotlin.reflect.KClass

interface AtpSerializer {

    fun toDiscriminator(cls: KClass<out AtpSchemaDef>): JsonPrimitive = JsonPrimitive("AtpSchemaDef.${cls.simpleName}")

    fun toDataMapper(type: String): KClass<out AtpSchemaDef> {
        return when (type) {
            "array" -> AtpSchemaDef.AtpArray::class
            "blob" -> AtpSchemaDef.AtpBlob::class
            "boolean" -> AtpSchemaDef.AtpBoolean::class
            "bytes" -> AtpSchemaDef.AtpBytes::class
            "cid-link" -> AtpSchemaDef.AtpCidLink::class
            "integer" -> AtpSchemaDef.AtpInteger::class
            "null" -> AtpSchemaDef.AtpNull::class
            "object" -> AtpSchemaDef.AtpObject::class
            "params" -> AtpSchemaDef.AtpParams::class
            "procedure" -> AtpSchemaDef.AtpProcedure::class
            "query" -> AtpSchemaDef.AtpQuery::class
            "record" -> AtpSchemaDef.AtpRecord::class
            "ref" -> AtpSchemaDef.AtpRef::class
            "string" -> AtpSchemaDef.AtpString::class
            "subscription" -> AtpSchemaDef.AtpSubscription::class
            "token" -> AtpSchemaDef.AtpToken::class
            "union" -> AtpSchemaDef.AtpUnion::class
            "unknown" -> AtpSchemaDef.AtpUnknown::class
            else -> throw IllegalArgumentException("Unmapped type: $type")
        }
    }
}

internal object AtpLexiconDefsSerializer: JsonTransformingSerializer<Map<String, AtpSchemaDef.PrimaryType>>(
    MapSerializer(String.serializer(), AtpSchemaDef.PrimaryType.serializer())
), AtpSerializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val type = this["type"]!!.jsonPrimitive.content
                require(type in listOf("object", "procedure", "query", "record", "subscription"))
                this["discriminator"] = toDiscriminator(toDataMapper(type))
            })
        }
        return JsonObject(content = content)
    }
}

internal object InputSchemaSerializer: JsonTransformingSerializer<AtpIo.Schema>(AtpIo.Schema.serializer()), AtpSerializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            val type = this["type"]!!.jsonPrimitive.content
            require(type in listOf("object", "ref", "union"))
            this["discriminator"] = toDiscriminator(toDataMapper(type))
        })
    }
}

internal object AtpParamsPropertiesSerializer: JsonTransformingSerializer<Map<String, AtpSchemaDef>>(MapSerializer(String.serializer(), AtpSchemaDef.serializer())), AtpSerializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val type = this["type"]!!.jsonPrimitive.content
                require(type in listOf("array", "boolean", "integer", "string", "unknown"))
                this["discriminator"] = toDiscriminator(toDataMapper(type))
            })
        }
        return JsonObject(content = content)
    }
}

internal object ObjectPropertiesSerializer: JsonTransformingSerializer<Map<String, AtpSchemaDef>>(MapSerializer(String.serializer(), AtpSchemaDef.serializer())), AtpSerializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val type = this["type"]!!.jsonPrimitive.content
                this["discriminator"] = toDiscriminator(toDataMapper(type))
            })
        }
        return JsonObject(content = content)
    }
}

internal object FieldTypeSerializer: JsonTransformingSerializer<AtpSchemaDef.FieldType>(AtpSchemaDef.FieldType.serializer()), AtpSerializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            val type = this["type"]!!.jsonPrimitive.content
            this["discriminator"] = toDiscriminator(toDataMapper(type))
        })
    }
}