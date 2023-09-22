import kotlinx.serialization.Serializable

@JvmRecord
@Serializable
data class Configuration(
    val name: String,
    val studySessionDuration: DurationRange,
    val breakSessionDuration: DurationRange
)
