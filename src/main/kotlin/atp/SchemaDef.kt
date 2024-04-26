package atp

import kotlinx.serialization.Serializable

@Serializable
sealed interface SchemaDef {
    val description: kotlin.String?
    val type: kotlin.String

    // allows schema definitions to be used in multiple contexts
    @Serializable
    sealed interface FieldType: SchemaDef
    @Serializable
    sealed interface PrimaryType: SchemaDef

    // https://atproto.com/specs/lexicon#array
    @Serializable
    data class Array(
        override val description: kotlin.String? = null,
        @Serializable(with = FieldTypeSerializer::class) val items: FieldType,
        val maxLength: Int? = null,
        val minLength: Int? = null,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#blob
    @Serializable
    data class Blob(
        val accept: List<kotlin.String>? = null,
        override val description: kotlin.String? = null,
        val maxSize: Int? = null,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#boolean
    @Serializable
    data class Boolean(
        val const: kotlin.Boolean? = null,
        val default: kotlin.Boolean? = null,
        override val description: kotlin.String? = null,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#bytes
    @Serializable
    data class Bytes(
        override val description: kotlin.String? = null,
        val maxLength: Int? = null,
        val minLength: Int? = null,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#cid-link
    @Serializable
    data class CidLink(
        override val description: kotlin.String? = null,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#integer
    @Serializable
    data class Integer(
        val const: Int? = null,
        val default: Int? = null,
        override val description: kotlin.String? = null,
        val enum: List<Int>? = null,
        val maximum: Int? = null,
        val minimum: Int? = null,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#null
    @Serializable
    data class Null(
        override val description: kotlin.String? = null,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#object
    @Serializable
    data class Object(
        override val description: kotlin.String? = null,
        val nullable: List<kotlin.String>? = null,
        @Serializable(with = ObjectPropertiesSerializer::class) val properties: Map<kotlin.String, SchemaDef>,
        val required: List<kotlin.String>? = null,
        override val type: kotlin.String
    ): FieldType, PrimaryType, IO.Schema

    // https://atproto.com/specs/lexicon#params
    @Serializable
    data class Params(
        override val description: kotlin.String? = null,
        @Serializable(with = ParamsPropertiesSerializer::class) val properties: Map<kotlin.String, SchemaDef>,
        val required: List<kotlin.String>? = null,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#query-and-procedure-http-api
    @Serializable
    data class Procedure(
        override val description: kotlin.String? = null,
        val errors: List<Error>? = null,
        val input: IO? = null,
        val parameters: Params? = null,
        val output: IO? = null,
        override val type: kotlin.String
    ): PrimaryType

    // https://atproto.com/specs/lexicon#query-and-procedure-http-api
    @Serializable
    data class Query(
        override val description: kotlin.String? = null,
        val errors: List<Error>? = null,
        val parameters: Params? = null,
        val output: IO? = null,
        override val type: kotlin.String
    ): PrimaryType

    // https://atproto.com/specs/lexicon#record
    @Serializable
    data class Record(
        override val description: kotlin.String? = null,
        val key: kotlin.String,
        val record: Object,
        override val type: kotlin.String
    ): PrimaryType

    // https://atproto.com/specs/lexicon#ref
    @Serializable
    data class Ref(
        override val description: kotlin.String? = null,
        val ref: kotlin.String,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#string
    @Serializable
    data class String(
        val const: kotlin.String? = null,
        val default: kotlin.String? = null,
        override val description: kotlin.String? = null,
        val enum: List<kotlin.String>? = null,
        val format: kotlin.String? = null, // https://atproto.com/specs/lexicon#string-formats
        val knownValues: List<Int>? = null,
        val maxGraphemes: Int? = null,
        val minGraphemes: Int? = null,
        val maxLength: Int? = null,
        val minLength: Int? = null,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#subscription-event-stream
    @Serializable
    data class Subscription(
        override val description: kotlin.String? = null,
        val errors: List<Error>? = null,
        val message: Message? = null,
        val parameters: Params? = null,
        override val type: kotlin.String
    ): PrimaryType {
        @Serializable
        data class Message(
            val description: kotlin.String? = null,
            val schema: Union
        )
    }

    // https://atproto.com/specs/lexicon#token
    @Serializable
    data class Token(
        override val description: kotlin.String? = null,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#union
    @Serializable
    data class Union(
        val closed: kotlin.Boolean? = null,
        override val description: kotlin.String? = null,
        val refs: List<kotlin.String>,
        override val type: kotlin.String
    ): FieldType

    // https://atproto.com/specs/lexicon#unknown
    @Serializable
    data class Unknown(
        override val description: kotlin.String? = null,
        override val type: kotlin.String
    ): FieldType
}


