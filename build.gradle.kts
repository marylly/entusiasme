import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by extra { "1.2.4" }

buildscript {
    extra.set("kotlinVersion", "1.3.50")

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${extra.get("kotlinVersion")}")
    }
}

plugins {
	val kotlinVersion = "1.3.50"
	
    id("org.jetbrains.kotlin.jvm").version("1.3.21")

    application
}

group = "entusiasme"
version = "1.0.0-SNAPSHOT"

repositories {
    jcenter()
	mavenCentral()
}

dependencies {
	implementation("org.slf4j:slf4j-api:1.7.2")
	implementation("org.slf4j:slf4j-simple:1.7.2")
	
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
	testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

application {
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
