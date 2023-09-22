import kotlinx.serialization.Serializable

@JvmRecord
@Serializable
data class Settings(
    val servers: Set<Long>
)



