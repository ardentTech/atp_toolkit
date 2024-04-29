package atp

import DISCRIMINATOR_KEY
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonPrimitive
import kotlin.reflect.KClass

internal const val SERIALIZER_PREFIX = "atp.SchemaDef"
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
        SchemaDefType.ARRAY -> SchemaDef.Array::class
        SchemaDefType.BLOB -> SchemaDef.Blob::class
        SchemaDefType.BOOLEAN -> SchemaDef.Boolean::class
        SchemaDefType.BYTES -> SchemaDef.Bytes::class
        SchemaDefType.CID_LINK -> SchemaDef.CidLink::class
        SchemaDefType.INTEGER -> SchemaDef.Integer::class
        SchemaDefType.NULL -> SchemaDef.Null::class
        SchemaDefType.OBJECT -> SchemaDef.Object::class
        SchemaDefType.PARAMS -> SchemaDef.Params::class
        SchemaDefType.PROCEDURE -> SchemaDef.Procedure::class
        SchemaDefType.QUERY -> SchemaDef.Query::class
        SchemaDefType.RECORD -> SchemaDef.Record::class
        SchemaDefType.REF -> SchemaDef.Ref::class
        SchemaDefType.STRING -> SchemaDef.String::class
        SchemaDefType.SUBSCRIPTION -> SchemaDef.Subscription::class
        SchemaDefType.TOKEN -> SchemaDef.Token::class
        SchemaDefType.UNION -> SchemaDef.Union::class
        SchemaDefType.UNKNOWN -> SchemaDef.Unknown::class
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