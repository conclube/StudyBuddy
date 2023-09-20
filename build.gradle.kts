import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.libsDirectory
import org.jetbrains.kotlin.incremental.createDirectory
import java.nio.file.Files

plugins {
    this.kotlin("jvm") version "1.9.0"
    this.kotlin("plugin.serialization") version "1.9.10"
    this.`java-library`
    this.id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.conclure.kth"
version = "1.0-SNAPSHOT"

repositories {
    this.mavenCentral()
}

dependencies {
    this.testImplementation(this.kotlin("test"))
    this.implementation("org.javacord:javacord:3.8.0")
    this.implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    this.runtimeOnly("org.apache.logging.log4j:log4j-core:2.20.0")

}

tasks.test {
    this.useJUnitPlatform()
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

kotlin {
    this.jvmToolchain(17)
}

tasks.shadowJar {
    this.manifest.attributes["Main-class"] = "Main"
}

val shadowJar = tasks.getByName<ShadowJar>("shadowJar")

val runDir = rootProject.projectDir.resolve("run")
val prepareDirectory = tasks.create<DefaultTask>("prepareDirectory") {
    this.group = "environment"
    this.setOnlyIf {
        !runDir.exists()
    }
    this.doFirst {
        runDir.createDirectory()
    }
}

val prepareJar = tasks.create<Copy>("prepareJar") {
    this.group = "environment"
    this.dependsOn(prepareDirectory,shadowJar)
    this.from(libsDirectory.get().file(shadowJar.archiveFileName))
    this.into(runDir)
}

tasks.create<Exec>("run") {
    this.group = "environment"
    this.dependsOn(prepareJar)
    this.workingDir = runDir
    this.commandLine = listOf("java","-jar", shadowJar.archiveFileName.get())
    this.standardInput = System.`in`
    this.standardOutput = System.out
}