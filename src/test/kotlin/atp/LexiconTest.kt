package atp

import AtpToolkit
import LexiconIO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LexiconTest {
    @Test
    fun `must have at least one def`() {
        assertFailsWith<IllegalArgumentException> {
            Lexicon(
                defs = mapOf(),
                id = "foobar",
                lexicon = 1
            )
        }
    }

    @Test
    fun `can have at most one definition with one of the primary types`() {
        assertFailsWith<IllegalArgumentException> {
            Lexicon(
                defs = mapOf(
                    "one" to SchemaDef.Query(),
                    "two" to SchemaDef.Query(),
                ),
                id = "foobar",
                lexicon = 1
            )
        }
    }

    // https://raw.githubusercontent.com/bluesky-social/atproto/main/lexicons/com/atproto/server/defs.json
    @Test
    fun `main defs equality`() {
        val rawJson = """
{
  "lexicon": 1,
  "id": "com.atproto.server.defs",
  "defs": {
    "inviteCode": {
      "type": "object",
      "required": [
        "code",
        "available",
        "disabled",
        "forAccount",
        "createdBy",
        "createdAt",
        "uses"
      ],
      "properties": {
        "code": { "type": "string" },
        "available": { "type": "integer" },
        "disabled": { "type": "boolean" },
        "forAccount": { "type": "string" },
        "createdBy": { "type": "string" },
        "createdAt": { "type": "string", "format": "datetime" },
        "uses": {
          "type": "array",
          "items": { "type": "ref", "ref": "#inviteCodeUse" }
        }
      }
    },
    "inviteCodeUse": {
      "type": "object",
      "required": ["usedBy", "usedAt"],
      "properties": {
        "usedBy": { "type": "string", "format": "did" },
        "usedAt": { "type": "string", "format": "datetime" }
      }
    }
  }
}
            
        """.trimIndent()
        val lexicon = Lexicon(
            id = "com.atproto.server.defs",
            lexicon = 1,
            defs = mapOf(
                "inviteCode" to SchemaDef.Object(
                    required = listOf(
                        "code",
                        "available",
                        "disabled",
                        "forAccount",
                        "createdBy",
                        "createdAt",
                        "uses"
                    ),
                    properties = mapOf(
                        "code" to SchemaDef.String(),
                        "available" to SchemaDef.Integer(),
                        "disabled" to SchemaDef.Boolean(),
                        "forAccount" to SchemaDef.String(),
                        "createdBy" to SchemaDef.String(),
                        "createdAt" to SchemaDef.String(format = "datetime"),
                        "uses" to SchemaDef.Array(items = SchemaDef.Ref(ref = "#inviteCodeUse")),
                    )
                ),
                "inviteCodeUse" to SchemaDef.Object(
                    required = listOf("usedBy", "usedAt"),
                    properties = mapOf(
                        "usedBy" to SchemaDef.String(format = "did"),
                        "usedAt" to SchemaDef.String(format = "datetime"),
                    )
                )
            )
        )

        val decoded = LexiconIO.read(rawJson, AtpToolkit.json)
        assertEquals(lexicon, decoded)
    }

    // https://raw.githubusercontent.com/bluesky-social/atproto/main/lexicons/com/atproto/server/activateAccount.json
    @Test
    fun `main procedure equality`() {
        val rawJson = """
{
  "lexicon": 1,
  "id": "com.atproto.server.activateAccount",
  "defs": {
    "main": {
      "type": "procedure",
      "description": "Activates a currently deactivated account. Used to finalize account migration after the account's repo is imported and identity is setup."
    }
  }
}            
        """.trimIndent()
        val lexicon = Lexicon(
            id = "com.atproto.server.activateAccount",
            lexicon = 1,
            defs = mapOf(
                "main" to SchemaDef.Procedure(
                    description = "Activates a currently deactivated account. Used to finalize account migration after the account's repo is imported and identity is setup."
                )
            )
        )

        val decoded = LexiconIO.read(rawJson, AtpToolkit.json)
        assertEquals(lexicon, decoded)
    }

    // https://raw.githubusercontent.com/bluesky-social/atproto/1c616b023e67760bd196825f09845c300fd15bea/lexicons/app/bsky/feed/like.json
    @Test
    fun `main record equality`() {
        val rawJson = """
{
  "lexicon": 1,
  "id": "app.bsky.feed.like",
  "defs": {
    "main": {
      "type": "record",
      "description": "Record declaring a 'like' of a piece of subject content.",
      "key": "tid",
      "record": {
        "type": "object",
        "required": ["subject", "createdAt"],
        "properties": {
          "subject": { "type": "ref", "ref": "com.atproto.repo.strongRef" },
          "createdAt": { "type": "string", "format": "datetime" }
        }
      }
    }
  }
}
        """.trimIndent()
        val lexicon = Lexicon(
            id = "app.bsky.feed.like",
            lexicon = 1,
            defs = mapOf(
                "main" to SchemaDef.Record(
                    description = "Record declaring a 'like' of a piece of subject content.",
                    key = "tid",
                    record = SchemaDef.Object(
                        required = listOf("subject", "createdAt"),
                        properties = mapOf(
                            "subject" to SchemaDef.Ref(ref = "com.atproto.repo.strongRef"),
                            "createdAt" to SchemaDef.String(format = "datetime")
                        )
                    )
                )
            )
        )

        val decoded = LexiconIO.read(rawJson, AtpToolkit.json)
        assertEquals(lexicon, decoded)
    }

    // https://raw.githubusercontent.com/bluesky-social/atproto/1c616b023e67760bd196825f09845c300fd15bea/lexicons/com/atproto/label/subscribeLabels.json
    @Test
    fun `main subscription equality`() {
        val rawJson = """
{
  "lexicon": 1,
  "id": "com.atproto.label.subscribeLabels",
  "defs": {
    "main": {
      "type": "subscription",
      "description": "Subscribe to stream of labels (and negations). Public endpoint implemented by mod services. Uses same sequencing scheme as repo event stream.",
      "parameters": {
        "type": "params",
        "properties": {
          "cursor": {
            "type": "integer",
            "description": "The last known event seq number to backfill from."
          }
        }
      },
      "message": {
        "schema": {
          "type": "union",
          "refs": ["#labels", "#info"]
        }
      },
      "errors": [{ "name": "FutureCursor" }]
    },
    "labels": {
      "type": "object",
      "required": ["seq", "labels"],
      "properties": {
        "seq": { "type": "integer" },
        "labels": {
          "type": "array",
          "items": { "type": "ref", "ref": "com.atproto.label.defs#label" }
        }
      }
    },
    "info": {
      "type": "object",
      "required": ["name"],
      "properties": {
        "name": {
          "type": "string",
          "knownValues": ["OutdatedCursor"]
        },
        "message": {
          "type": "string"
        }
      }
    }
  }
}

        """.trimIndent()
        val lexicon = Lexicon(
            id = "com.atproto.label.subscribeLabels",
            lexicon = 1,
            defs = mapOf(
                "main" to SchemaDef.Subscription(
                    description = "Subscribe to stream of labels (and negations). Public endpoint implemented by mod services. Uses same sequencing scheme as repo event stream.",
                    parameters = SchemaDef.Params(
                        properties = mapOf(
                            "cursor" to SchemaDef.Integer(description = "The last known event seq number to backfill from.")
                        )
                    ),
                    message = SchemaDef.Subscription.Message(
                        schema = SchemaDef.Union(
                            refs = listOf("#labels", "#info")
                        )
                    ),
                    errors = listOf(
                        Error(name = "FutureCursor")
                    )
                ),
                "labels" to SchemaDef.Object(
                    required = listOf("seq", "labels"),
                    properties = mapOf(
                        "seq" to SchemaDef.Integer(),
                        "labels" to SchemaDef.Array(
                            items = SchemaDef.Ref(ref = "com.atproto.label.defs#label")
                        )
                    )
                ),
                "info" to SchemaDef.Object(
                    required = listOf("name"),
                    properties = mapOf(
                        "name" to SchemaDef.String(
                            knownValues = listOf("OutdatedCursor")
                        ),
                        "message" to SchemaDef.String()
                    )
                )
            )
        )

        val decoded = LexiconIO.read(rawJson, AtpToolkit.json)
        assertEquals(lexicon, decoded)
    }

    // https://raw.githubusercontent.com/bluesky-social/atproto/main/lexicons/com/atproto/server/getSession.json
    @Test
    fun `main query equality`() {
        val rawJson = """
{
  "lexicon": 1,
  "id": "com.atproto.server.getSession",
  "defs": {
    "main": {
      "type": "query",
      "description": "Get information about the current auth session. Requires auth.",
      "output": {
        "encoding": "application/json",
        "schema": {
          "type": "object",
          "required": ["handle", "did"],
          "properties": {
            "handle": { "type": "string", "format": "handle" },
            "did": { "type": "string", "format": "did" },
            "email": { "type": "string" },
            "emailConfirmed": { "type": "boolean" },
            "emailAuthFactor": { "type": "boolean" },
            "didDoc": { "type": "unknown" }
          }
        }
      }
    }
  }
}            
        """.trimIndent()

        val lexicon = Lexicon(
            id = "com.atproto.server.getSession",
            lexicon = 1,
            defs = mapOf(
                "main" to SchemaDef.Query(
                    description = "Get information about the current auth session. Requires auth.",
                    output = IO(
                        encoding = "application/json",
                        schema = SchemaDef.Object(
                            required = listOf("handle", "did"),
                            properties = mapOf(
                                "handle" to SchemaDef.String(
                                    format = "handle"
                                ),
                                "did" to SchemaDef.String(
                                    format = "did"
                                ),
                                "email" to SchemaDef.String(),
                                "emailConfirmed" to SchemaDef.Boolean(),
                                "emailAuthFactor" to SchemaDef.Boolean(),
                                "didDoc" to SchemaDef.Unknown()
                            )
                        )
                    )
                )
            )
        )

        val decoded = LexiconIO.read(rawJson, AtpToolkit.json)
        assertEquals(lexicon, decoded)
    }
}