import kotlinx.serialization.Serializable
import kotlin.time.Duration

@JvmRecord
@Serializable
data class DurationRange(
    val min: String,
    val max: String
) {
    fun minAsDuration(): Duration {
        return Duration.parseIsoString(this.min)
    }
    fun maxAsDuration(): Duration {
        return Duration.parseIsoString(this.max)
    }
}
