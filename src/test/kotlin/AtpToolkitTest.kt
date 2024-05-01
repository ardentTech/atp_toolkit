import atp.Lexicon
import io.ktor.client.plugins.*
import io.mockk.*
import kotlinx.coroutines.test.runTest
import java.io.File
import kotlin.reflect.KClass
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AtpToolkitTest {

    @AfterTest
    fun tearDown() { clearAllMocks() }

    private inline fun <reified T: Exception> configureNetworkIOError(errorMsg: String) {
        val exc = mockk<T>()
        every { exc.message } returns errorMsg
        mockkObject(LexiconIO)
        coEvery { LexiconIO.read(client = any(), json = any(), url = any()) } throws exc
    }

    private inline fun <reified T: Exception> assertResultFailure(res: Result<Any>, errorMsg: String?) {
        assert(res.isFailure)
        val error = res.exceptionOrNull()
        assert(error is T)
        assertEquals(errorMsg, error!!.message)
    }

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
    fun `readLexicon url failure, client error`() = runTest {
        val msg = "foobar"
        configureNetworkIOError<ClientRequestException>(msg)
        val res = AtpToolkit().readLexicon("http://testing.com")
        assertResultFailure<AtpToolkitClientError>(res, msg)
    }

    @Test
    fun `readLexicon url failure, redirect error`() = runTest {
        val msg = "foobar"
        configureNetworkIOError<RedirectResponseException>(msg)
        val res = AtpToolkit().readLexicon("http://testing.com")
        assertResultFailure<AtpToolkitRedirectError>(res, msg)
    }

    @Test
    fun `readLexicon url failure, server error`() = runTest {
        val msg = "foobar"
        configureNetworkIOError<ServerResponseException>(msg)
        val res = AtpToolkit().readLexicon("http://testing.com")
        assertResultFailure<AtpToolkitServerError>(res, msg)
    }

    @Test
    fun `readLexicon url failure, unexpected error`() = runTest {
        val msg = "foobar"
        configureNetworkIOError<IllegalArgumentException>(msg)
        val res = AtpToolkit().readLexicon("http://testing.com")
        assertResultFailure<AtpToolkitUnexpectedError>(res, msg)
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