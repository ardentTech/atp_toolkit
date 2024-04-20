import kotlin.test.Test
import kotlin.test.assertFailsWith

class DomainTest {

    @Test
    fun `lexicon must have at least one def`() {
        assertFailsWith<IllegalArgumentException> {
            AtpLexicon(
                defs = mapOf(),
                id = "foobar",
                lexicon = 1
            )
        }
    }

    @Test
    fun `lexicon can have at most one definition with one of the primary types`() {
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