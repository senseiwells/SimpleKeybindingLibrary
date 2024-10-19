plugins {
    alias(libs.plugins.fabric.loom)
    `maven-publish`
    java
}

repositories {
    mavenCentral()
    maven("https://maven.parchmentmc.org/")
    maven("https://maven.isxander.dev/releases")
    maven("https://jitpack.io")
}

val modVersion = "0.1.11"
version = "${modVersion}+${libs.versions.minecraft.get()}"
group = "me.senseiwells"

dependencies {
    minecraft(libs.minecraft)
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.parchment.get()}@zip")
    })

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    modCompileOnly(libs.yacl)
}

java {
    withSourcesJar()
}

loom {
    accessWidenerPath.set(file("src/main/resources/simple-keybinding-library.accesswidener"))
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf(
                "version" to project.version,
                "minecraft_dependency" to libs.versions.minecraft.get().replaceAfterLast('.', "x"),
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

private fun MavenPublication.updateReadme(vararg readmes: String) {
    val location = "${groupId}:${artifactId}"
    val regex = Regex("""${Regex.escape(location)}:[\d\.\-a-zA-Z+]+""")
    val locationWithVersion = "${location}:${version}"
    for (path in readmes) {
        val readme = file(path)
        readme.writeText(readme.readText().replace(regex, locationWithVersion))
    }
}