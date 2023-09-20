import kotlinx.serialization.Serializable

@Serializable
data class Configuration(val name: String, val studySessionDuration: DurationRange, val breakSessionDuration: DurationRange)
