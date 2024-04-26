package atp

import kotlin.test.Test
import kotlin.test.assertFailsWith

class LexiconTest {
    @Test
    fun `must have at least one def`() {
        assertFailsWith<IllegalArgumentException> {
            Lexicon(
                defs = mapOf(),
                id = "foobar",
                lexicon = 1
            )
        }
    }

    @Test
    fun `can have at most one definition with one of the primary types`() {
        assertFailsWith<IllegalArgumentException> {
            Lexicon(
                defs = mapOf(
                    "one" to SchemaDef.Query(type = "query"),
                    "two" to SchemaDef.Query(type = "query"),
                ),
                id = "foobar",
                lexicon = 1
            )
        }
    }
}