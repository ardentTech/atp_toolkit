package atp

import kotlinx.serialization.Serializable

object SchemaDefType {
    const val ARRAY = "array"
    const val BLOB = "blob"
    const val BOOLEAN = "boolean"
    const val BYTES = "bytes"
    const val CID_LINK = "cid-link"
    const val INTEGER = "integer"
    const val NULL = "null"
    const val OBJECT = "object"
    const val PARAMS = "params"
    const val PROCEDURE = "procedure"
    const val QUERY = "query"
    const val RECORD = "record"
    const val REF = "ref"
    const val STRING = "string"
    const val SUBSCRIPTION = "subscription"
    const val TOKEN = "token"
    const val UNION = "union"
    const val UNKNOWN = "unknown"
}

@Serializable
sealed interface SchemaDef {
    val description: kotlin.String?
    val type: kotlin.String

    // TODO could have a hybrid (e.g. PrimaryFieldType)

    // allows schema definitions to be used in multiple contexts
    @Serializable
    sealed interface FieldType: SchemaDef
    @Serializable
    sealed interface PrimaryType: SchemaDef

    // https://atproto.com/specs/lexicon#array
    @Serializable
    data class Array(
        override val description: kotlin.String? = null,
        @Serializable(with = atp.FieldType::class) val items: FieldType,
        val maxLength: Int? = null,
        val minLength: Int? = null
    ): FieldType {
        override val type = SchemaDefType.ARRAY
    }

    // https://atproto.com/specs/lexicon#blob
    @Serializable
    data class Blob(
        val accept: List<kotlin.String>? = null,
        override val description: kotlin.String? = null,
        val maxSize: Int? = null
    ): FieldType {
        override val type = SchemaDefType.BLOB
    }

    // https://atproto.com/specs/lexicon#boolean
    @Serializable
    data class Boolean(
        val const: kotlin.Boolean? = null,
        val default: kotlin.Boolean? = null,
        override val description: kotlin.String? = null
    ): FieldType {
        override val type = SchemaDefType.BOOLEAN
    }

    // https://atproto.com/specs/lexicon#bytes
    @Serializable
    data class Bytes(
        override val description: kotlin.String? = null,
        val maxLength: Int? = null,
        val minLength: Int? = null
    ): FieldType {
        override val type = SchemaDefType.BYTES
    }

    // https://atproto.com/specs/lexicon#cid-link
    @Serializable
    data class CidLink(
        override val description: kotlin.String? = null
    ): FieldType {
        override val type = SchemaDefType.CID_LINK
    }

    // https://atproto.com/specs/lexicon#integer
    @Serializable
    data class Integer(
        val const: Int? = null,
        val default: Int? = null,
        override val description: kotlin.String? = null,
        val enum: List<Int>? = null,
        val maximum: Int? = null,
        val minimum: Int? = null
    ): FieldType {
        override val type = SchemaDefType.INTEGER
    }

    // https://atproto.com/specs/lexicon#null
    @Serializable
    data class Null(
        override val description: kotlin.String? = null
    ): FieldType {
        override val type = SchemaDefType.NULL
    }

    // https://atproto.com/specs/lexicon#object
    @Serializable
    data class Object(
        override val description: kotlin.String? = null,
        val nullable: List<kotlin.String>? = null,
        @Serializable(with = ObjectProperties::class) val properties: Map<kotlin.String, SchemaDef>,
        val required: List<kotlin.String>? = null
    ): FieldType, PrimaryType, IO.Schema {
        override val type = SchemaDefType.OBJECT
    }

    // https://atproto.com/specs/lexicon#params
    @Serializable
    data class Params(
        override val description: kotlin.String? = null,
        @Serializable(with = ParamsProperties::class) val properties: Map<kotlin.String, SchemaDef>,
        val required: List<kotlin.String>? = null
    ): FieldType {
        override val type = SchemaDefType.PARAMS
    }

    // https://atproto.com/specs/lexicon#query-and-procedure-http-api
    @Serializable
    data class Procedure(
        override val description: kotlin.String? = null,
        val errors: List<Error>? = null,
        val input: IO? = null,
        val parameters: Params? = null,
        val output: IO? = null
    ): PrimaryType {
        override val type = SchemaDefType.PROCEDURE
    }

    // https://atproto.com/specs/lexicon#query-and-procedure-http-api
    @Serializable
    data class Query(
        override val description: kotlin.String? = null,
        val errors: List<Error>? = null,
        val parameters: Params? = null,
        val output: IO? = null
    ): PrimaryType {
        override val type = SchemaDefType.QUERY
    }

    // https://atproto.com/specs/lexicon#record
    @Serializable
    data class Record(
        override val description: kotlin.String? = null,
        val key: kotlin.String,
        val record: Object
    ): PrimaryType {
        override val type = SchemaDefType.RECORD
    }

    // https://atproto.com/specs/lexicon#ref
    @Serializable
    data class Ref(
        override val description: kotlin.String? = null,
        val ref: kotlin.String
    ): FieldType, IO.Schema {
        override val type = SchemaDefType.REF
    }

    // https://atproto.com/specs/lexicon#string
    @Serializable
    data class String(
        val const: kotlin.String? = null,
        val default: kotlin.String? = null,
        override val description: kotlin.String? = null,
        val enum: List<kotlin.String>? = null,
        val format: kotlin.String? = null, // TODO https://atproto.com/specs/lexicon#string-formats
        val knownValues: List<kotlin.String>? = null,
        val maxGraphemes: Int? = null,
        val minGraphemes: Int? = null,
        val maxLength: Int? = null,
        val minLength: Int? = null,
    ): FieldType {
        override val type = SchemaDefType.STRING
    }

    // https://atproto.com/specs/lexicon#subscription-event-stream
    @Serializable
    data class Subscription(
        override val description: kotlin.String? = null,
        val errors: List<Error>? = null,
        val message: Message? = null,
        val parameters: Params? = null
    ): PrimaryType {
        override val type = SchemaDefType.SUBSCRIPTION
        @Serializable
        data class Message(
            val description: kotlin.String? = null,
            val schema: Union
        )
    }

    // https://atproto.com/specs/lexicon#token
    @Serializable
    data class Token(
        override val description: kotlin.String? = null
    ): FieldType {
        override val type = SchemaDefType.TOKEN
    }

    // https://atproto.com/specs/lexicon#union
    @Serializable
    data class Union(
        val closed: kotlin.Boolean? = null,
        override val description: kotlin.String? = null,
        val refs: List<kotlin.String>
    ): FieldType, IO.Schema {
        override val type = SchemaDefType.UNION
    }

    // https://atproto.com/specs/lexicon#unknown
    @Serializable
    data class Unknown(
        override val description: kotlin.String? = null
    ): FieldType {
        override val type = SchemaDefType.UNKNOWN
    }
}


