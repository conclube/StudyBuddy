import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import java.time.temporal.ChronoUnit

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                FileSystems.newFileSystem(File(Main::class.java.protectionDomain.codeSource.location.path).toPath()).use {
                    val settingsFile = Path.of(".").resolve("settings.json")
                    if (Files.notExists(settingsFile)) {
                        Files.copy(it.getPath("settings.json"), settingsFile)
                    }

                    val configurationsDir = Path.of(".").resolve("configurations")
                    if (Files.notExists(configurationsDir)) {
                        Files.createDirectory(configurationsDir)
                    }

                    val defaultConfigurationFile = configurationsDir.resolve("default.json")
                    if (Files.notExists(defaultConfigurationFile)) {
                        Files.copy(it.getPath("configurations", "default.json"), defaultConfigurationFile)
                    }
                }

                // Try adding program arguments via Run/Debug configuration.
                // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
                println("Program arguments: ${args.joinToString()}")
                println(
                    Duration.of(
                        30, ChronoUnit.MINUTES
                    )
                );
                //ZipFileSystem
            } catch (e: Throwable){
                e.printStackTrace()
            }
        }
    }
}