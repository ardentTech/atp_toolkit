package atp

import DISCRIMINATOR_KEY
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*
import kotlin.reflect.KClass

private const val SERIALIZER_PREFIX = "data.SchemaDef"
private const val TYPE_KEY = "type"

interface Serializer {

    fun buildDiscriminator(cls: KClass<out SchemaDef>): JsonPrimitive {
        return JsonPrimitive("$SERIALIZER_PREFIX.${cls.simpleName}")
    }

    fun getDataMapper(type: String): KClass<out SchemaDef> {
        return when (type) {
            // TODO abstract to standalone map?
            LexiconType.ARRAY -> SchemaDef.Array::class
            LexiconType.BLOB -> SchemaDef.Blob::class
            LexiconType.BOOLEAN -> SchemaDef.Boolean::class
            LexiconType.BYTES -> SchemaDef.Bytes::class
            LexiconType.CID_LINK -> SchemaDef.CidLink::class
            LexiconType.INTEGER -> SchemaDef.Integer::class
            LexiconType.NULL -> SchemaDef.Null::class
            LexiconType.OBJECT -> SchemaDef.Object::class
            LexiconType.PARAMS -> SchemaDef.Params::class
            LexiconType.PROCEDURE -> SchemaDef.Procedure::class
            LexiconType.QUERY -> SchemaDef.Query::class
            LexiconType.RECORD -> SchemaDef.Record::class
            LexiconType.REF -> SchemaDef.Ref::class
            LexiconType.STRING -> SchemaDef.String::class
            LexiconType.SUBSCRIPTION -> SchemaDef.Subscription::class
            LexiconType.TOKEN -> SchemaDef.Token::class
            LexiconType.UNION -> SchemaDef.Union::class
            LexiconType.UNKNOWN -> SchemaDef.Unknown::class
            else -> throw IllegalArgumentException("Unmapped type: $type")
        }
    }

    fun getType(map: MutableMap<String, JsonElement>) = map[TYPE_KEY]!!.jsonPrimitive.content

    fun setDiscriminator(cls: KClass<out SchemaDef>, map: MutableMap<String, JsonElement>) {
        map[DISCRIMINATOR_KEY] = buildDiscriminator(cls)
    }
}

// TODO unit test
internal object LexiconDefsSerializer: JsonTransformingSerializer<Map<String, SchemaDef.PrimaryType>>(
    MapSerializer(String.serializer(), SchemaDef.PrimaryType.serializer())
), Serializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val type = getType(this)
                // TODO unit test
                require(type in listOf(
                    LexiconType.OBJECT,
                    LexiconType.PROCEDURE,
                    LexiconType.QUERY,
                    LexiconType.RECORD,
                    LexiconType.SUBSCRIPTION
                ))
                setDiscriminator(getDataMapper(type), this)
            })
        }
        return JsonObject(content = content)
    }
}

// TODO unit test
internal object InputSchemaSerializer: JsonTransformingSerializer<IO.Schema>(IO.Schema.serializer()),
    Serializer {
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
internal object ParamsPropertiesSerializer: JsonTransformingSerializer<Map<String, SchemaDef>>(MapSerializer(String.serializer(), SchemaDef.serializer())),
    Serializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        val content = mutableMapOf<String, JsonElement>()
        element.jsonObject.map { (k, v) ->
            content[k] = JsonObject(v.jsonObject.toMutableMap().apply {
                val type = getType(this)
                // TODO unit test
                require(type in listOf(
                    LexiconType.ARRAY,
                    LexiconType.BOOLEAN,
                    LexiconType.INTEGER,
                    LexiconType.STRING,
                    LexiconType.UNKNOWN
                ))
                setDiscriminator(getDataMapper(type), this)
            })
        }
        return JsonObject(content = content)
    }
}

// TODO unit test
internal object ObjectPropertiesSerializer: JsonTransformingSerializer<Map<String, SchemaDef>>(MapSerializer(String.serializer(), SchemaDef.serializer())),
    Serializer {
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
internal object FieldTypeSerializer: JsonTransformingSerializer<SchemaDef.FieldType>(SchemaDef.FieldType.serializer()),
    Serializer {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            setDiscriminator(getDataMapper(getType(this)), this)
        })
    }
}