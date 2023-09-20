class ConfigurationRepository(private val map: MutableMap<String, Configuration>) : Iterable<Configuration>{

    companion object {
        @JvmStatic
        fun create(): ConfigurationRepository {
            return ConfigurationRepository(HashMap())
        }
    }

    fun registerConfiguration(configuration: Configuration): Boolean {
        try {
            configuration.studySessionDuration.maxAsDuration()
            configuration.studySessionDuration.minAsDuration()
            configuration.breakSessionDuration.maxAsDuration()
            configuration.breakSessionDuration.minAsDuration()
        } catch (e: IllegalArgumentException) {
            return false;
        }
        if (this.map[configuration.name] == null) {
            this.map[configuration.name] = configuration
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
}