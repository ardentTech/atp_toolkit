import atp.Lexicon
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import java.io.File

internal const val DISCRIMINATOR_KEY = "discriminator"

class AtpToolkit {
    companion object {
        internal val json by lazy {
            Json {
                classDiscriminator = DISCRIMINATOR_KEY
                classDiscriminatorMode = ClassDiscriminatorMode.NONE
                ignoreUnknownKeys = false
            }
        }
    }

    private val client by lazy {
        HttpClient( CIO) {
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
            Result.failure(when (e) {
                is RedirectResponseException -> AtpToolkitRedirectError(e.message)
                is ClientRequestException -> AtpToolkitClientError(e.message)
                is ServerResponseException -> AtpToolkitServerError(e.message)
                else -> AtpToolkitUnexpectedError(e.message)
            })
        }
}