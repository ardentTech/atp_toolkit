package atp

import DISCRIMINATOR_KEY
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonPrimitive
import kotlin.reflect.KClass

internal const val SERIALIZER_PREFIX = "data.SchemaDef"
internal const val TYPE_KEY = "type"

typealias JsonMap = MutableMap<String, JsonElement>

// TODO could restrict this
internal abstract class BaseSerializer<T: Any>(tSerializer: KSerializer<T>): JsonTransformingSerializer<T>(tSerializer) {

    open val validTypes: List<String>? = null

    fun addDiscriminator(map: JsonMap) {
        val type = getType(map)
        validateType(type)
        setDiscriminator(map, getSerializerCls(type))
    }

    fun getSerializerCls(type: String): KClass<out SchemaDef> = when (type) {
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

    fun getType(map: JsonMap): String = map[TYPE_KEY]!!.jsonPrimitive.content

    fun setDiscriminator(map: JsonMap, serializerCls: KClass<out SchemaDef>) {
        map[DISCRIMINATOR_KEY] = JsonPrimitive("$SERIALIZER_PREFIX.${serializerCls.simpleName}")
    }

    fun validateType(type: String) {
        validTypes?.let {
            require(type in it)
        }
    }
}