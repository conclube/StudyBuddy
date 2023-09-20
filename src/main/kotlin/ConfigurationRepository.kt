class ConfigurationRepository(private val map: MutableMap<String, Configuration>) {

    companion object {
        @JvmStatic
        fun create(): ConfigurationRepository {
            return ConfigurationRepository(HashMap())
        }
    }

    fun registerConfiguration(configuration: Configuration) {
        if (this.map[configuration.name] == null) {
            this.map[configuration.name] = configuration
        }
    }

    fun deregisterConfiguration(configurationName: String) {
        this.map.remove(configurationName)
    }
}