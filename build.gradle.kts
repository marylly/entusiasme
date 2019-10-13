import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by extra { "1.2.4" }

buildscript {
    extra.set("kotlinVersion", "1.3.50")

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra.get("kotlinVersion")}")
    }
}

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 */
plugins {
	val kotlinVersion = "1.3.50"
	
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    id("org.jetbrains.kotlin.jvm").version("1.3.21")

    // Apply the application plugin to add support for building a CLI application.
    application
}

group = "entusiasme"
version = "1.0.0-SNAPSHOT"

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
	mavenCentral()
}

dependencies {
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.8.1")
	
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
	
	testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

application {
    // Define the main class for the application.
    mainClassName = "entusiasme.AppKt"
}

val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.34.2")
}

var ktlintTask = tasks.register<JavaExec>("ktlint") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("src/main/**/*.kt", "src/test/**/*.kt", "src/endtoend-test/**/*.kt",
        "src/integration-test/**/*.kt")
}

tasks.register<JavaExec>("ktlintFormat") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Fix Kotlin code style deviations"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("-F", "src/main/**/*.kt", "src/test/**/*.kt", "src/endtoend-test/**/*.kt",
        "src/integration-test/**/*.kt")
}

tasks.named("check").get().dependsOn(ktlintTask)
