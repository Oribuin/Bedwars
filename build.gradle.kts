import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.launcher.daemon.protocol.Build

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("maven-publish")
}

group = "in.oribu"
version = "1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isFork = true
    options.compilerArgs.add("-parameters")
}

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://hub.jeff-media.com/nexus/repository/jeff-media-public/")
    maven("https://jitpack.io/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    api("dev.rosewood:rosegarden:1.2.5")
    api("org.jetbrains:annotations:24.0.0")

    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")

    // Plugin Dependencies
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.0-SNAPSHOT") {
        exclude(group = "org.github")
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier = null

    relocate("org.jetbrains.annotations", "in.oribu.bedwars.libs.annotations")
    relocate("dev.rosewood.rosegarden", "in.oribu.bedwars.libs.rosegarden")

    exclude("dev/rosewood/rosegarden/lib/hikaricp/**/*.class")
    exclude("dev/rosewood/rosegarden/lib/slf4j/**/*.class")
}

tasks {

    shadowJar {
        this.archiveClassifier = null

        this.relocate("org.jetbrains.annotations", "in.oribu.bedwars.libs.annotations")
        this.relocate("dev.rosewood.rosegarden", "in.oribu.bedwars.libs.rosegarden")

        this.exclude("dev/rosewood/rosegarden/lib/hikaricp/**/*.class")
        this.exclude("dev/rosewood/rosegarden/lib/slf4j/**/*.class")
    }

    build {
        this.dependsOn("shadowJar")
    }

}