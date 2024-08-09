plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("maven-publish")
}

group = "in.oribu"
version = "1.0.0"

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
    api("dev.rosewood:rosegarden:1.4.0")
    api("dev.triumphteam:triumph-gui:3.1.7") {
        exclude(group = "com.google.code.gson", module = "gson")
        exclude(group = "net.kyori", module = "*")
    }

    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
//    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")

    // Plugin Dependencies
    compileOnly("com.mojang:authlib:1.5.21")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.0-SNAPSHOT")
    compileOnly("com.arcaniax:HeadDatabase-API:1.3.1")
}

tasks {

    shadowJar {
        this.archiveClassifier.set("")

        this.relocate("dev.rosewood.rosegarden", "in.oribu.bedwars.libs.rosegarden")
        this.relocate("dev.triumphteam.gui", "in.oribu.bedwars.libs.gui")

        this.exclude("dev/rosewood/rosegarden/lib/hikaricp/**/*.class")
        this.exclude("dev/rosewood/rosegarden/lib/slf4j/**/*.class")
    }

    build {
        this.dependsOn("shadowJar")
    }

}