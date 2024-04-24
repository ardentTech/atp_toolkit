import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import java.io.File

internal const val JSON_SUFFIX = ".json"
internal const val NOT_JSON_ERROR_MSG = "Remote file format must be JSON."

object AtpLexiconIo {
    fun read(file: File, json: Json): AtpLexicon =
        json.decodeFromString(
            file.inputStream().bufferedReader().use { it.readText() }
        )

    suspend fun read(client: HttpClient, json: Json, url: String): AtpLexicon {
        if (!url.endsWith(JSON_SUFFIX)) { throw IllegalArgumentException(NOT_JSON_ERROR_MSG) }
        return json.decodeFromString(client.get(url).bodyAsText())
    }
}