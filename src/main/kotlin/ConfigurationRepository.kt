import java.nio.file.Path
import kotlin.io.path.name

class ConfigurationRepository(
    private val map: MutableMap<String, Configuration>
) : Iterable<Configuration>{

    companion object {
        @JvmStatic
        fun create(): ConfigurationRepository {
            return ConfigurationRepository(HashMap())
        }
    }

    fun registerConfiguration(fileName: String, configuration: Configuration): Boolean {
        try {
            configuration.studySessionDuration.maxAsDuration()
            configuration.studySessionDuration.minAsDuration()
            configuration.breakSessionDuration.maxAsDuration()
            configuration.breakSessionDuration.minAsDuration()
        } catch (e: IllegalArgumentException) {
            return false;
        }
        if (fileName.isEmpty()) {
            return false;
        }
        if (this.map[fileName] == null) {
            this.map[fileName] = configuration
            return true
        }
        return false
    }

    fun deregisterConfiguration(configurationName: String): Boolean{
        return this.map.remove(configurationName) == null;
    }

    override fun iterator(): Iterator<Configuration> {
        return this.map.values.iterator()
    }

    override fun toString(): String {
        return this.map.toString();
    }
}