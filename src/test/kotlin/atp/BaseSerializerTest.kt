package atp

import DISCRIMINATOR_KEY
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BaseSerializerTest {

    @Test
    fun `addDiscriminator ok`() {
        val type = SchemaDefType.INTEGER
        val map: JsonMap = mutableMapOf("type" to JsonPrimitive(type))
        val serializer = object: BaseSerializer<String>(String.serializer()) {
            override val validTypes = listOf(type)
        }
        serializer.addDiscriminator(map)
        assertEquals("atp.SchemaDef.Integer", (map[DISCRIMINATOR_KEY] as JsonPrimitive).content)
    }

    @Test
    fun `getSerializerCls unmapped type`() {
        assertFailsWith<IllegalArgumentException> {
            val serializer = object: BaseSerializer<String>(String.serializer()) {}
            serializer.getSerializerCls("invalid")
        }
    }

    @Test
    fun `getSerializerCls ok`() {
        val serializer = object: BaseSerializer<String>(String.serializer()) {}
        val cls = serializer.getSerializerCls(SchemaDefType.BOOLEAN)
        assertEquals(SchemaDef.Boolean::class, cls)
    }

    @Test
    fun `getType no type key`() {
        assertFailsWith<NullPointerException> {
            val serializer = object: BaseSerializer<String>(String.serializer()) {}
            serializer.getType(mutableMapOf())
        }
    }

    @Test
    fun `getType ok`() {
        val serializer = object: BaseSerializer<String>(String.serializer()) {}
        val type = SchemaDefType.BOOLEAN
        val res = serializer.getType(mutableMapOf(TYPE_KEY to JsonPrimitive(type)))
        assertEquals(type, res)
    }

    @Test
    fun `setDiscriminator ok`() {
        val map: JsonMap = mutableMapOf()
        val serializer = object: BaseSerializer<String>(String.serializer()) {}
        serializer.setDiscriminator(map, SchemaDef.Boolean::class)
        assertEquals("atp.SchemaDef.Boolean", (map[DISCRIMINATOR_KEY] as JsonPrimitive).content)
    }

    @Test
    fun `validateType validTypes null`() {
        val serializer = object: BaseSerializer<String>(String.serializer()) {
            override val validTypes = null
        }
        serializer.validateType(SchemaDefType.STRING)
    }

    @Test
    fun `validateType validTypes empty`() {
        assertFailsWith<IllegalArgumentException> {
            val serializer = object: BaseSerializer<String>(String.serializer()) {
                override val validTypes = emptyList<String>()
            }
            serializer.validateType(SchemaDefType.STRING)
        }
    }

    @Test
    fun `validateType, validTypes populated, type invalid`() {
        assertFailsWith<IllegalArgumentException> {
            val serializer = object: BaseSerializer<String>(String.serializer()) {
                override val validTypes = listOf(SchemaDefType.BOOLEAN)
            }
            serializer.validateType(SchemaDefType.STRING)
        }
    }

    @Test
    fun `validateType, validTypes populated, type valid`() {
        val serializer = object: BaseSerializer<String>(String.serializer()) {
            override val validTypes = listOf(SchemaDefType.STRING)
        }
        serializer.validateType(SchemaDefType.STRING)
    }
}