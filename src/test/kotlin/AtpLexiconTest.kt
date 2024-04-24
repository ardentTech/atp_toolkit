import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AtpLexiconTest {

    @Test
    fun `build id cannot be null`() {
        val res = assertFailsWith<IllegalStateException> {
            val builder = AtpLexicon.Builder()
            builder.build()
        }
        assertEquals(res.message, AtpLexicon.Builder.ID_CANNOT_BE_NULL)
    }

    @Test
    fun `build lexicon cannot be null`() {
        val res = assertFailsWith<IllegalStateException> {
            val builder = AtpLexicon.Builder(id = "foo.bar")
            builder.build()
        }
        assertEquals(res.message, AtpLexicon.Builder.LEXICON_CANNOT_BE_NULL)
    }

    @Test
    fun `must have at least one def`() {
        assertFailsWith<IllegalArgumentException> {
            AtpLexicon(
                defs = mapOf(),
                id = "foobar",
                lexicon = 1
            )
        }
    }

    @Test
    fun `can have at most one definition with one of the primary types`() {
        assertFailsWith<IllegalArgumentException> {
            AtpLexicon(
                defs = mapOf(
                    "one" to AtpSchemaDef.AtpQuery(type = "query"),
                    "two" to AtpSchemaDef.AtpQuery(type = "query"),
                ),
                id = "foobar",
                lexicon = 1
            )
        }
    }
}