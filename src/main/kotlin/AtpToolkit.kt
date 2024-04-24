import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import java.io.File

internal const val DISCRIMINATOR_KEY = "discriminator"

class AtpToolkit(
    private val engine: HttpClientEngine = CIO.create()
) {
    private val client by lazy {
        HttpClient(engine) {
            expectSuccess = true
        }
    }

    private val json by lazy {
        Json {
            classDiscriminator = DISCRIMINATOR_KEY
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
            ignoreUnknownKeys = false // TODO set to `true` after validation
            prettyPrint = true
        }
    }

    fun readLexicon(file: File): Result<AtpLexicon> =
        try {
            Result.success(AtpLexiconIo.read(file, json))
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun readLexicon(url: String): Result<AtpLexicon> =
        try {
            Result.success(AtpLexiconIo.read(client, json, url))
        } catch (e: Exception) {
            Result.failure(e)
        }
}