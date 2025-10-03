import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	kotlin("jvm") version "2.2.0"
	kotlin("plugin.serialization") version "2.2.0"
	id("fabric-loom") version "1.11-SNAPSHOT"
}

version = property("mod_version")!!
group = property("maven_group")!!

dependencies {
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")

	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand(mapOf("version" to inputs.properties["version"]))
	}
}

java {
	sourceCompatibility = JavaVersion.VERSION_21
	targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
	compilerOptions {
		jvmTarget.set(JvmTarget.JVM_21)
	}
}

tasks.jar {
	inputs.property("archivesName", project.base.archivesName.get())

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archivesName"]}" }
	}
}
