package atp

import AtpToolkit
import kotlin.test.Test

class SchemaDefTest {

    @Test
    fun `array deserialize ok`() {
        val payload = """
{
    "type": "array",
    "description": "Annotations of text (mentions, URLs, hashtags, etc)",
    "items": { "type": "ref", "ref": "app.bsky.richtext.facet" }
}            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Array.serializer(), payload)
    }

    @Test
    fun `blob deserialize ok`() {
        val payload = """
{ "type": "blob" }            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Blob.serializer(), payload)
    }

    @Test
    fun `boolean deserialize ok`() {
        val payload = """
{
    "type": "boolean"
}     
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Boolean.serializer(), payload)
    }

    @Test
    fun `bytes deserialize ok`() {
        val payload = """
{ "type": "bytes" }            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Bytes.serializer(), payload)
    }

    @Test
    fun `cid-link deserialize ok`() {
        val payload = """
{ "type": "cid-link" }            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.CidLink.serializer(), payload)
    }

    @Test
    fun `integer deserialize ok`() {
        val payload = """
{
    "type": "integer"
}     
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Integer.serializer(), payload)
    }

    @Test
    fun `null deserialize ok`() {
        val payload = """
{
    "type": "null"
}     
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Null.serializer(), payload)
    }

    @Test
    fun `object deserialize ok`() {
        val payload = """
{
    "type": "object",
    "properties": {}
}            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Object.serializer(), payload)
    }

    @Test
    fun `params deserialize ok`() {
        val payload = """
{
    "type": "params",
    "properties": {}
}            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Params.serializer(), payload)
    }

    @Test
    fun `procedure deserialize ok`() {
        val payload = """
{ "type": "procedure" }            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Procedure.serializer(), payload)
    }

    @Test
    fun `query deserialize ok`() {
        val payload = """
{ "type": "query" }            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Query.serializer(), payload)
    }

    @Test
    fun `record deserialize ok`() {
        val payload = """
{
    "type": "record",
    "description": "Record declaring a 'like' of a piece of subject content.",
    "key": "tid",
    "record": {
        "type": "object",
        "properties": {}
    }
}            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Record.serializer(), payload)
    }

    @Test
    fun `ref deserialize ok`() {
        val payload = """
{
    "type": "ref",
    "ref": "com.atproto.server.defs#inviteCode"
}     
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Ref.serializer(), payload)
    }

    @Test
    fun `string deserialize ok`() {
        val payload = """
{
    "type": "string"
}     
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.String.serializer(), payload)
    }

    @Test
    fun `subscription deserialize ok`() {
        val payload = """
{ "type": "subscription" }            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Subscription.serializer(), payload)
    }

    @Test
    fun `token deserialize ok`() {
        val payload = """
{
    "type": "token",
    "description": "Spam: frequent unwanted promotion, replies, mentions"
}           
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Token.serializer(), payload)
    }

    @Test
    fun `union deserialize ok`() {
        val payload = """
{
    "type": "union",
    "refs": ["com.atproto.server.defs#inviteCode"]
}     
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Union.serializer(), payload)
    }

    @Test
    fun `unknown deserialize ok`() {
        val payload = """
{ "type": "unknown" }            
        """.trimIndent()
        AtpToolkit.json.decodeFromString(SchemaDef.Unknown.serializer(), payload)
    }
}