package atp

import AtpToolkit
import kotlin.test.Test
import kotlin.test.assertFails

private const val ENCODING_JSON = "application/json"

class IOSchemaTest {

    @Test
    fun `allows object type`() {
        val str = """
{
  "encoding": "$ENCODING_JSON",
  "schema": {
    "type": "${SchemaDefType.OBJECT}",
    "properties": {}
  }
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(IO.serializer(), str)
    }

    @Test
    fun `allows ref type`() {
        val str = """
{
  "encoding": "$ENCODING_JSON",
  "schema": {
    "type": "${SchemaDefType.REF}",
    "ref": ""
  }
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(IO.serializer(), str)
    }

    @Test
    fun `allows union type`() {
        val str = """
{
  "encoding": "$ENCODING_JSON",
  "schema": {
    "type": "${SchemaDefType.UNION}",
    "refs": []
  }
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(IO.serializer(), str)
    }

    // TODO find way to introduce random unsupported types
    @Test
    fun `rejects other types`() {
        assertFails {
            val str = """
{
  "encoding": "$ENCODING_JSON",
  "schema": {
    "type": "${SchemaDefType.BOOLEAN}"
  }
}
            """.trimIndent()
            AtpToolkit.json.decodeFromString(IO.serializer(), str)
        }
    }
}