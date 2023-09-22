import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.io.path.name

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val botBuilder = Bot.builder()
            try {
                val settingsFile = Path.of(".").resolve("settings.json")
                val configurationsDir = Path.of(".").resolve("configurations")
                val defaultConfigurationFile = configurationsDir.resolve("default.json")
                //TODO find a way to avoid File.java
                val path = File(Main::class.java.protectionDomain.codeSource.location.path).toPath()
                FileSystems.newFileSystem(path).use {
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
                botBuilder.repository(repository)
                Files.walkFileTree(configurationsDir,ConfigurationFileRegistrationVisitor(repository))

                Files.newInputStream(settingsFile).use {
                    @OptIn(ExperimentalSerializationApi::class)
                    botBuilder.settings(Json.decodeFromStream<Settings>(it));
                }

            } catch (e: Throwable){
                e.printStackTrace()
            }
            val bot = botBuilder.build();
            println(bot)
        }
    }

    class ConfigurationFileRegistrationVisitor(private val repository: ConfigurationRepository) : FileVisitor<Path> {
        override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes?): FileVisitResult {
            return FileVisitResult.CONTINUE
        }

        @OptIn(ExperimentalSerializationApi::class)
        override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
            val name = file.name
            if (name.endsWith(".json")) {
                Files.newInputStream(file).use {
                    this.repository.registerConfiguration(name.substring(0,name.length-5), Json.decodeFromStream<Configuration>(it))
                }
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