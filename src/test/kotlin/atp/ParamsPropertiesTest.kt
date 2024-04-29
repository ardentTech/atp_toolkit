package atp

import AtpToolkit
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

class ParamsPropertiesTest {

    @Test
    fun `allows array type`() {
        val str = """
{
   "properties": {
     "foobar": {
       "items": {
          "type": "${SchemaDefType.OBJECT}",
          "properties": {}
        },
        "type": "${SchemaDefType.ARRAY}"
     }
   },
   "type": "${SchemaDefType.PARAMS}"
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Params.serializer(), str)
    }

    @Test
    fun `allows boolean type`() {
        val str = """
{
   "properties": {
     "foobar": {
        "type": "${SchemaDefType.BOOLEAN}"
     }
   },
   "type": "${SchemaDefType.PARAMS}"
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Params.serializer(), str)
    }

    @Test
    fun `allows integer type`() {
        val str = """
{
   "properties": {
     "foobar": {
        "type": "${SchemaDefType.INTEGER}"
     }
   },
   "type": "${SchemaDefType.PARAMS}"
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Params.serializer(), str)
    }

    @Test
    fun `allows string type`() {
        val str = """
{
   "properties": {
     "foobar": {
        "type": "${SchemaDefType.STRING}"
     }
   },
   "type": "${SchemaDefType.PARAMS}"
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Params.serializer(), str)
    }

    @Test
    fun `allows unknown type`() {
        val str = """
{
   "properties": {
     "foobar": {
        "type": "${SchemaDefType.UNKNOWN}"
     }
   },
   "type": "${SchemaDefType.PARAMS}"
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Params.serializer(), str)
    }

    @Test
    fun `rejects other types`() {
        assertFails {
            val str = """
{
   "properties": {
     "type": "${SchemaDefType.OBJECT}",
     "properties": {}
   },
   "type": "${SchemaDefType.PARAMS}"
}
            """.trimIndent()
            AtpToolkit.json.decodeFromString(SchemaDef.Params.serializer(), str)
        }
    }
}