import io.mockk.*
import kotlinx.coroutines.test.runTest
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class AtpToolkitTest {

    @Test
    fun `readLexicon file failure`() {
        val exc = Exception()
        mockkObject(AtpLexiconIo)
        every { AtpLexiconIo.read(file = any(), json = any()) } throws exc
        val res = AtpToolkit().readLexicon(file = File("/path/to/file"))

        assert(res.isFailure)
        assertEquals(res.exceptionOrNull(), exc)
    }

    @Test
    fun `readLexicon file success`() {
        val lex = mockk<AtpLexicon>()
        mockkObject(AtpLexiconIo)
        every { AtpLexiconIo.read(file = any(), json = any()) } returns lex
        val res = AtpToolkit().readLexicon(file = File("/path/to/file"))

        assert(res.isSuccess)
        assertEquals(res.getOrThrow(), lex)
    }

    @Test
    fun `readLexicon url failure`() = runTest {
        val exc = Exception()
        val url = "testing"
        mockkObject(AtpLexiconIo)
        coEvery { AtpLexiconIo.read(client = any(), json = any(), url = url) } throws exc
        val res = AtpToolkit().readLexicon(url)

        assert(res.isFailure)
        assertEquals(res.exceptionOrNull(), exc)
    }

    @Test
    fun `readLexicon url success`() = runTest {
        val lex = mockk<AtpLexicon>()
        val url = "testing"
        mockkObject(AtpLexiconIo)
        coEvery { AtpLexiconIo.read(client = any(), json = any(), url = url) } returns lex
        val res = AtpToolkit().readLexicon(url)

        assert(res.isSuccess)
        assertEquals(res.getOrThrow(), lex)
    }
}