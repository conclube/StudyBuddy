import kotlinx.serialization.Serializable

class Bot(
    val settings: Settings,
    val repository: ConfigurationRepository
) {

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }

    data class Builder(
        private var settings: Settings? = null,
        private var repository: ConfigurationRepository? = null
    ) {

        fun settings(settings: Settings) = this.apply { this.settings = settings }
        fun repository(repository: ConfigurationRepository) = this.apply { this.repository = repository }
        fun build() = Bot(this.settings!!, this.repository!!)
    }

    override fun toString(): String {
        return "{settings=${this.settings}, repository=${this.repository}}"
    }
}