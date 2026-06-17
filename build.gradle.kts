plugins {
    alias(libs.plugins.fabric.loom)
    `maven-publish`
    java
}

repositories {
    mavenCentral()
    maven("https://maven.parchmentmc.org/")
    maven("https://maven.isxander.dev/releases")
    maven("https://api.modrinth.com/maven")
    maven("https://jitpack.io")
}

val modVersion = "0.5.0"
version = "${modVersion}+${libs.versions.minecraft.get()}"
group = "me.senseiwells"

dependencies {
    minecraft(libs.minecraft)

    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)

    compileOnly(libs.controlling)
    compileOnly(libs.searchables)
    // localRuntime(libs.controlling)
    // localRuntime(libs.searchables)

    compileOnly(libs.yacl)
}

java {
    withSourcesJar()
}

val testmod: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().compileClasspath
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

loom {
    accessWidenerPath.set(file("src/main/resources/simple-keybinding-library.classtweaker"))

    runs {
        create("testmodClient") {
            client()
            sourceSet.set(testmod.name)
            jvmArguments.add("-Dmixin.debug.export=true")
        }
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf(
                "version" to project.version,
                "minecraft_dependency" to replaceVersion(libs.versions.minecraft.get(), "x"),
                "fabric_loader_dependency" to libs.versions.fabric.loader.get(),
            ))
        }
    }

    jar {
        from("LICENSE")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(project.components.getByName("java"))
            artifactId = "simple-keybinding-library"

            updateReadme("./README.md")
        }
    }

    repositories {
        val mavenUrl = System.getenv("MAVEN_URL")
        if (mavenUrl != null) {
            maven {
                url = uri(mavenUrl)
                val mavenUsername = System.getenv("MAVEN_USERNAME")
                val mavenPassword = System.getenv("MAVEN_PASSWORD")
                if (mavenUsername != null && mavenPassword != null) {
                    credentials {
                        username = mavenUsername
                        password = mavenPassword
                    }
                }
            }
        }
    }
}

fun replaceVersion(version: String, patch: String): String {
    return version.replace(Regex("""^(\d+\.\d+)(\.\d+)?$"""), "$1.$patch")
}

private fun MavenPublication.updateReadme(vararg readmes: String) {
    val location = "${groupId}:${artifactId}"
    val regex = Regex("""${Regex.escape(location)}:[\d\.\-a-zA-Z+]+""")
    val locationWithVersion = "${location}:${version}"
    for (path in readmes) {
        val readme = file(path)
        readme.writeText(readme.readText().replace(regex, locationWithVersion))
    }
}