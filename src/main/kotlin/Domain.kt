import kotlinx.serialization.Serializable

// TODO move all string literals to constants

@Serializable
data class AtpError(
    val description: String? = null,
    val name: String
)

@Serializable
data class AtpIo(
    val description: String? = null,
    val encoding: String,
    @Serializable(with = InputSchemaSerializer::class) val schema: Schema? = null
) {
    @Serializable
    sealed interface Schema
}

@Serializable
sealed interface AtpSchemaDef {
    val description: String?
    val type: String

    // allows schema definitions to be used in multiple contexts
    @Serializable
    sealed interface FieldType: AtpSchemaDef
    @Serializable
    sealed interface PrimaryType: AtpSchemaDef

    // https://atproto.com/specs/lexicon#array
    @Serializable
    data class AtpArray(
        override val description: String? = null,
        @Serializable(with = FieldTypeSerializer::class) val items: FieldType,
        val maxLength: Int? = null,
        val minLength: Int? = null,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#blob
    @Serializable
    data class AtpBlob(
        val accept: List<String>? = null,
        override val description: String? = null,
        val maxSize: Int? = null,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#boolean
    @Serializable
    data class AtpBoolean(
        val const: Boolean? = null,
        val default: Boolean? = null,
        override val description: String? = null,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#bytes
    @Serializable
    data class AtpBytes(
        override val description: String? = null,
        val maxLength: Int? = null,
        val minLength: Int? = null,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#cid-link
    @Serializable
    data class AtpCidLink(
        override val description: String? = null,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#integer
    @Serializable
    data class AtpInteger(
        val const: Int? = null,
        val default: Int? = null,
        override val description: String? = null,
        val enum: List<Int>? = null,
        val maximum: Int? = null,
        val minimum: Int? = null,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#null
    @Serializable
    data class AtpNull(
        override val description: String? = null,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#object
    @Serializable
    data class AtpObject(
        override val description: String? = null,
        val nullable: List<String>? = null,
        @Serializable(with = ObjectPropertiesSerializer::class) val properties: Map<String, AtpSchemaDef>,
        val required: List<String>? = null,
        override val type: String
    ): FieldType, PrimaryType, AtpIo.Schema

    // https://atproto.com/specs/lexicon#params
    @Serializable
    data class AtpParams(
        override val description: String? = null,
        @Serializable(with = AtpParamsPropertiesSerializer::class) val properties: Map<String, AtpSchemaDef>,
        val required: List<String>? = null,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#query-and-procedure-http-api
    @Serializable
    data class AtpProcedure(
        override val description: String? = null,
        val errors: List<AtpError>? = null,
        val input: AtpIo? = null,
        val parameters: AtpParams? = null,
        val output: AtpIo? = null,
        override val type: String
    ): PrimaryType

    // https://atproto.com/specs/lexicon#query-and-procedure-http-api
    @Serializable
    data class AtpQuery(
        override val description: String? = null,
        val errors: List<AtpError>? = null,
        val parameters: AtpParams? = null,
        val output: AtpIo? = null,
        override val type: String
    ): PrimaryType

    // https://atproto.com/specs/lexicon#record
    @Serializable
    data class AtpRecord(
        override val description: String? = null,
        val key: String,
        val record: AtpObject,
        override val type: String
    ): PrimaryType

    // https://atproto.com/specs/lexicon#ref
    @Serializable
    data class AtpRef(
        override val description: String? = null,
        val ref: String,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#string
    @Serializable
    data class AtpString(
        val const: String? = null,
        val default: String? = null,
        override val description: String? = null,
        val enum: List<String>? = null,
        val format: String? = null, // https://atproto.com/specs/lexicon#string-formats
        val knownValues: List<Int>? = null,
        val maxGraphemes: Int? = null,
        val minGraphemes: Int? = null,
        val maxLength: Int? = null,
        val minLength: Int? = null,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#subscription-event-stream
    @Serializable
    data class AtpSubscription(
        override val description: String? = null,
        val errors: List<AtpError>? = null,
        val message: Message? = null,
        val parameters: AtpParams? = null,
        override val type: String
    ): PrimaryType {
        @Serializable
        data class Message(
            val description: String? = null,
            val schema: AtpUnion
        )
    }

    // https://atproto.com/specs/lexicon#token
    @Serializable
    data class AtpToken(
        override val description: String? = null,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#union
    @Serializable
    data class AtpUnion(
        val closed: Boolean? = null,
        override val description: String? = null,
        val refs: List<String>,
        override val type: String
    ): FieldType

    // https://atproto.com/specs/lexicon#unknown
    @Serializable
    data class AtpUnknown(
        override val description: String? = null,
        override val type: String
    ): FieldType
}

@Serializable
data class AtpLexicon(
    @Serializable(with = AtpLexiconDefsSerializer::class) val defs: Map<String, AtpSchemaDef.PrimaryType>,
    val description: String? = null,
    val id: String,
    val lexicon: Int,
    val revision: Int? = null
) {
    init {
        require(defs.isNotEmpty())

        // allow multiple `object` defs but only one primary def
        var count = 0
        defs.forEach {
            if (it.value !is AtpSchemaDef.AtpObject) {
                count++
                if (count > 1) throw IllegalArgumentException()
            }
        }
    }
}