import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertFailsWith

class LexiconReaderTest {

    private val mockEngine = MockEngine { request ->
        respond(
            content = ByteReadChannel.Empty,
            status = HttpStatusCode.OK
        )
    }

    @Test
    fun `url must have json suffix`() = runTest {
        assertFailsWith<IllegalArgumentException> {
            LexiconReader.read(
                engine = mockEngine,
                json = Json {
                    classDiscriminator = "discriminator"
                    classDiscriminatorMode = ClassDiscriminatorMode.NONE
                    ignoreUnknownKeys = false
                    prettyPrint = true
                },
                url = "invalid url"
            )
        }
    }
}