package atp

import kotlin.test.Test
import kotlin.test.assertFails

class LexiconDefsTest {

    @Test
    fun `allows object type`() {
        val str = """
{
  "id": "foo.bar",
  "lexicon": 1,
  "defs": {
    "foo": {
      "type": "${SchemaDefType.OBJECT}",
      "properties": {}
    }
  }
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(Lexicon.serializer(), str)
    }

    @Test
    fun `allows procedure type`() {
        val str = """
{
  "id": "foo.bar",
  "lexicon": 1,
  "defs": {
    "foo": {
      "type": "${SchemaDefType.PROCEDURE}"
    }
  }
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(Lexicon.serializer(), str)
    }

    @Test
    fun `allows query type`() {
        val str = """
{
  "id": "foo.bar",
  "lexicon": 1,
  "defs": {
    "foo": {
      "type": "${SchemaDefType.QUERY}"
    }
  }
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(Lexicon.serializer(), str)
    }

    @Test
    fun `allows record type`() {
        val str = """
{
  "id": "foo.bar",
  "lexicon": 1,
  "defs": {
    "foo": {
      "type": "${SchemaDefType.RECORD}",
      "key": "testing",
      "record": {
        "type": "${SchemaDefType.OBJECT}",
        "properties": {}
      }
    }
  }
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(Lexicon.serializer(), str)
    }

    @Test
    fun `allows subscription type`() {
        val str = """
{
  "id": "foo.bar",
  "lexicon": 1,
  "defs": {
    "foo": {
      "type": "${SchemaDefType.SUBSCRIPTION}"
    }
  }
}
            """.trimIndent()
        AtpToolkit.json.decodeFromString(Lexicon.serializer(), str)
    }

    @Test
    fun `rejects other types`() {
        assertFails {
            val str = """
{
  "id": "foo.bar",
  "lexicon": 1,
  "defs": {
    "foo": {
      "type": "${SchemaDefType.INTEGER}"
    }
  }
}
            """.trimIndent()
            AtpToolkit.json.decodeFromString(Lexicon.serializer(), str)
        }
    }
}