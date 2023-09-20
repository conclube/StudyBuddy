import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.Duration
import java.time.temporal.ChronoUnit

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                val settingsFile = Path.of(".").resolve("settings.json")
                val configurationsDir = Path.of(".").resolve("configurations")
                val defaultConfigurationFile = configurationsDir.resolve("default.json")
                FileSystems.newFileSystem(File(Main::class.java.protectionDomain.codeSource.location.path).toPath()).use {
                    if (Files.notExists(settingsFile)) {
                        Files.copy(it.getPath("settings.json"), settingsFile)
                    }

                    if (Files.notExists(configurationsDir)) {
                        Files.createDirectory(configurationsDir)
                    }

                    if (Files.notExists(defaultConfigurationFile)) {
                        Files.copy(it.getPath("configurations", "default.json"), defaultConfigurationFile)
                    }
                }
                val repository = ConfigurationRepository.create()
                Files.walkFileTree(configurationsDir,ConfigurationFileVisitor(repository))
                for (configuration in repository) {
                    println(configuration)
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

    class ConfigurationFileVisitor(private val repository: ConfigurationRepository) : FileVisitor<Path> {
        override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes?): FileVisitResult {
            return FileVisitResult.CONTINUE
        }

        @OptIn(ExperimentalSerializationApi::class)
        override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
            Files.newInputStream(file).use {
                this.repository.registerConfiguration(Json.decodeFromStream<Configuration>(it))
            }
            return FileVisitResult.CONTINUE
        }

        override fun visitFileFailed(file: Path, exc: IOException?): FileVisitResult {
            return FileVisitResult.CONTINUE
        }

        override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
            return FileVisitResult.CONTINUE
        }

    }
}