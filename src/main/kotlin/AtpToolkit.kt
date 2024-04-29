import atp.Lexicon
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import java.io.File

internal const val DISCRIMINATOR_KEY = "discriminator"
internal const val JSON_SUFFIX = ".json"
internal const val NOT_JSON_ERROR_MSG = "Remote file format must be JSON."

class AtpToolkit(
    private val engine: HttpClientEngine = CIO.create()
) {
    companion object {
        val json by lazy {
            Json {
                classDiscriminator = DISCRIMINATOR_KEY
                classDiscriminatorMode = ClassDiscriminatorMode.NONE
                ignoreUnknownKeys = false // TODO set to `true` after validation
                prettyPrint = true
            }
        }
    }
    private val client by lazy {
        HttpClient(engine) {
            expectSuccess = true
        }
    }

    fun readLexicon(file: File): Result<Lexicon> =
        try {
            Result.success(LexiconIO.read(file, json))
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun readLexicon(url: String): Result<Lexicon> =
        try {
            Result.success(LexiconIO.read(client, json, url))
        } catch (e: Exception) {
            // TODO map non-200s to avoid leaking Ktor exception details
            Result.failure(e)
        }
}