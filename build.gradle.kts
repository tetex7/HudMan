/*
 * Copyright (C) 2024  Tete
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import org.gradle.kotlin.dsl.*
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.LocalDate;
import java.time.LocalTime
import com.trs.bobbuilder.ReleaseUtils

plugins {
    //kotlin("jvm") version "2.0.20"
    id("fabric-loom") version "1.7.1"
    id("maven-publish")
    //id("com.trs.bobbuilder")
}


//apply<BobBuilderPlugin>()

//val debug = ((project.property("debug") as String) == "y" || (project.property("debug") as String) == "yes")

version = "${(project.property("mod_version") as String)}-mc${project.property("minecraft_version")}"

group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 17
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}


repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
    maven(url = "https://maven.parchmentmc.org")
    //maven("https://maven.gegy.dev")
    //maven(url = "https://server.bbkr.space/artifactory/libs-release")
    /*maven(
        name = "ParchmentMC",
        url = "https://maven.parchmentmc.org"
    )*/
}



dependencies {
    // To change the versions see the gradle.properties file
    //loom.clientOnlyMinecraftJar()
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings().
        parchment("org.parchmentmc.data:parchment-1.20.1:2023.09.03@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    /*modImplementation(include("io.github.cottonmc:LibGui:8.1.0+1.20.1") ?: "")
    modImplementation(include("dev.lambdaurora:spruceui:5.0.0+1.20") ?: "")*/

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
}

fun standardizedPath(path: String): String
{
    return path.replace('\\', '/')
}


tasks.processResources pr@{
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version"),
            "loader_version" to project.property("loader_version"),
            "kotlin_loader_version" to project.property("kotlin_loader_version")
        )
    }

    doLast {
        ReleaseUtils.mkJsonMark(this@pr)
    }
}

tasks.register("buildJar") {
    group = "build"
    description = "Runs build, remapJar, and remapSourcesJar in sequence, then copies outputs."

    dependsOn("build", "remapJar", "remapSourcesJar")

    doLast {
        fun deleteDirectoryRecursively(directory: Path) {
            Files.walk(directory)
                .sorted(Comparator.reverseOrder())
                .forEach { path ->
                    Files.delete(path)
                }
        }

        val namej = project.property("archives_base_name") as String
        val buildOutPath = Paths.get("${project.rootDir}/build_out")
        val sourcesJarPath = Paths.get("${project.rootDir}/build/devlibs/${namej}-${project.version}-sources.jar")
        val jarPath = Paths.get("${project.rootDir}/build/libs/${namej}-${project.version}.jar")

        println("Checking existence of paths:")
        println("Source JAR path: $sourcesJarPath (exists: ${Files.exists(sourcesJarPath)})")
        println("JAR path: $jarPath (exists: ${Files.exists(jarPath)})")

        // Delete the output directory if it exists
        if (Files.exists(buildOutPath, LinkOption.NOFOLLOW_LINKS)) {
            deleteDirectoryRecursively(buildOutPath)
        }
        Files.createDirectories(buildOutPath)

        // Copy only if both source files exist
        if (Files.exists(sourcesJarPath) && Files.exists(jarPath)) {
            Files.copy(sourcesJarPath, buildOutPath.resolve(sourcesJarPath.fileName), StandardCopyOption.REPLACE_EXISTING)
            Files.copy(jarPath, buildOutPath.resolve(jarPath.fileName), StandardCopyOption.REPLACE_EXISTING)
            println("Files successfully copied to $buildOutPath")
        } else {
            throw IllegalStateException("One or both required JAR files are missing: $sourcesJarPath, $jarPath")
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

/*tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}*/

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName}" }
    }
}

tasks.clean.get().doLast {
    val buildOutPath = Paths.get("${project.rootDir}/build_out")
    fun deleteDirectoryRecursively(directory: Path) {
        Files.walk(directory)
            .sorted(Comparator.reverseOrder()) // Ensures files are deleted before the directory itself
            .forEach { path ->
                Files.delete(path)
            }
    }

    if (Files.exists(buildOutPath, LinkOption.NOFOLLOW_LINKS))
    {
        deleteDirectoryRecursively(buildOutPath)
    }
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}

