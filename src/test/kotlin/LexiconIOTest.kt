import io.ktor.client.*
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class LexiconIOTest {

    @AfterTest
    fun tearDown() { clearAllMocks() }

    @Test
    fun `url must have json suffix`() = runTest {
        assertFailsWith<IllegalArgumentException> {
            LexiconIO.read(mockk<HttpClient>(), mockk<Json>(), "http://localhost")
        }
    }
}