import atp.Lexicon
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import java.io.File

internal object LexiconIO {
    fun read(file: File, json: Json): Lexicon =
        json.decodeFromString(
            file.inputStream().bufferedReader().use { it.readText() }
        )

    suspend fun read(client: HttpClient, json: Json, url: String): Lexicon {
        if (!url.endsWith(JSON_SUFFIX)) { throw IllegalArgumentException(NOT_JSON_ERROR_MSG) }
        return json.decodeFromString(client.get(url).bodyAsText())
    }
}