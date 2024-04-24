import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import kotlin.reflect.KClass

private const val DATA_MAPPER_MODULE = "AtpSchemaDef"
private const val TYPE_KEY = "type"

interface AtpSerializer {

    fun buildDiscriminator(cls: KClass<out AtpSchemaDef>): JsonPrimitive {
        return JsonPrimitive("$DATA_MAPPER_MODULE.${cls.simpleName}")
    }

    fun getDataMapper(type: String): KClass<out AtpSchemaDef> {
        return when (type) {
            // TODO abstract to standalone map
            LexiconType.ARRAY -> AtpSchemaDef.AtpArray::class
            LexiconType.BLOB -> AtpSchemaDef.AtpBlob::class
            LexiconType.BOOLEAN -> AtpSchemaDef.AtpBoolean::class
            LexiconType.BYTES -> AtpSchemaDef.AtpBytes::class
            LexiconType.CID_LINK -> AtpSchemaDef.AtpCidLink::class
            LexiconType.INTEGER -> AtpSchemaDef.AtpInteger::class
            LexiconType.NULL -> AtpSchemaDef.AtpNull::class
            LexiconType.OBJECT -> AtpSchemaDef.AtpObject::class
            LexiconType.PARAMS -> AtpSchemaDef.AtpParams::class
            LexiconType.PROCEDURE -> AtpSchemaDef.AtpProcedure::class
            LexiconType.QUERY -> AtpSchemaDef.AtpQuery::class
            LexiconType.RECORD -> AtpSchemaDef.AtpRecord::class
            LexiconType.REF -> AtpSchemaDef.AtpRef::class
            LexiconType.STRING -> AtpSchemaDef.AtpString::class
            LexiconType.SUBSCRIPTION -> AtpSchemaDef.AtpSubscription::class
            LexiconType.TOKEN -> AtpSchemaDef.AtpToken::class
            LexiconType.UNION -> AtpSchemaDef.AtpUnion::class
            LexiconType.UNKNOWN -> AtpSchemaDef.AtpUnknown::class
            else -> throw IllegalArgumentException("Unmapped type: $type")
        }
    }

    fun getType(map: MutableMap<String, JsonElement>) = map[TYPE_KEY]!!.jsonPrimitive.content

    fun setDiscriminator(cls: KClass<out AtpSchemaDef>, map: MutableMap<String, JsonElement>) {
        map[DISCRIMINATOR_KEY] = buildDiscriminator(cls)
    }
}

// TODO unit test
internal object AtpLexiconDefsSerializer: JsonTransformingSerializer<Map<String, AtpSchemaDef.PrimaryType>>(
    MapSerializer(String.serializer(), AtpSchemaDef.PrimaryType.serializer())
), AtpSerializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val type = getType(this)
                // TODO unit test
                require(type in listOf(LexiconType.OBJECT, LexiconType.PROCEDURE, LexiconType.QUERY, LexiconType.RECORD, LexiconType.SUBSCRIPTION))
                setDiscriminator(getDataMapper(type), this)
            })
        }
        return JsonObject(content = content)
    }
}

// TODO unit test
internal object InputSchemaSerializer: JsonTransformingSerializer<AtpIo.Schema>(AtpIo.Schema.serializer()), AtpSerializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            val type = getType(this)
            // TODO unit test
            require(type in listOf(LexiconType.OBJECT, LexiconType.REF, LexiconType.UNION))
            setDiscriminator(getDataMapper(type), this)
        })
    }
}

// TODO unit test
internal object AtpParamsPropertiesSerializer: JsonTransformingSerializer<Map<String, AtpSchemaDef>>(MapSerializer(String.serializer(), AtpSchemaDef.serializer())), AtpSerializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val type = getType(this)
                // TODO unit test
                require(type in listOf(LexiconType.ARRAY, LexiconType.BOOLEAN, LexiconType.INTEGER, LexiconType.STRING, LexiconType.UNKNOWN))
                setDiscriminator(getDataMapper(type), this)
            })
        }
        return JsonObject(content = content)
    }
}

// TODO unit test
internal object ObjectPropertiesSerializer: JsonTransformingSerializer<Map<String, AtpSchemaDef>>(MapSerializer(String.serializer(), AtpSchemaDef.serializer())), AtpSerializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                setDiscriminator(getDataMapper(getType(this)), this)
            })
        }
        return JsonObject(content = content)
    }
}

// TODO unit test
internal object FieldTypeSerializer: JsonTransformingSerializer<AtpSchemaDef.FieldType>(AtpSchemaDef.FieldType.serializer()), AtpSerializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            setDiscriminator(getDataMapper(getType(this)), this)
        })
    }
}