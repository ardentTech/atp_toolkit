package atp

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal object FieldType: BaseSerializer<SchemaDef.FieldType>(SchemaDef.FieldType.serializer()){
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return JsonObject(element.jsonObject.toMutableMap().apply {
            addDiscriminator(this)
        })
    }
}