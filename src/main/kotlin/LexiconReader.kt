import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.Json

// TODO read local file
object LexiconReader {

    // inefficient as it creates a new HttpClient instance on each call
    suspend fun read(
        engine: HttpClientEngine = CIO.create(),
        json: Json,
        url: String
    ): AtpLexicon {
        return read(HttpClient(engine), json, url)
    }

    // caller manages client instance, but this leaks Ktor details
    suspend fun read(
        client: HttpClient,
        json: Json,
        url: String
    ): AtpLexicon {
        if (!url.endsWith(".json")) { throw IllegalArgumentException("Remote file format must be JSON.") }
        return json.decodeFromString(client.get(url).bodyAsText())
    }
}