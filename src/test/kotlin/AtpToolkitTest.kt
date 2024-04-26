import atp.Lexicon
import io.ktor.client.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AtpToolkitTest {
    @Test
    fun `readLexicon file failure`() {
        val exc = Exception()
        mockkObject(LexiconIO)
        every { LexiconIO.read(file = any(), json = any()) } throws exc
        val res = AtpToolkit().readLexicon(file = File("/path/to/file"))

        assert(res.isFailure)
        assertEquals(res.exceptionOrNull(), exc)
    }

    @Test
    fun `readLexicon file success`() {
        val lex = mockk<Lexicon>()
        mockkObject(LexiconIO)
        every { LexiconIO.read(file = any(), json = any()) } returns lex
        val res = AtpToolkit().readLexicon(file = File("/path/to/file"))

        assert(res.isSuccess)
        assertEquals(res.getOrThrow(), lex)
    }

    @Test
    fun `readLexicon url failure`() = runTest {
        val exc = Exception()
        val url = "testing"
        mockkObject(LexiconIO)
        coEvery { LexiconIO.read(client = any(), json = any(), url = url) } throws exc
        val res = AtpToolkit().readLexicon(url)

        assert(res.isFailure)
        assertEquals(res.exceptionOrNull(), exc)
    }

    @Test
    fun `readLexicon url success`() = runTest {
        val lex = mockk<Lexicon>()
        val url = "testing"
        mockkObject(LexiconIO)
        coEvery { LexiconIO.read(client = any(), json = any(), url = url) } returns lex
        val res = AtpToolkit().readLexicon(url)

        assert(res.isSuccess)
        assertEquals(res.getOrThrow(), lex)
    }
}