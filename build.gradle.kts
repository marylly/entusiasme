val ktorVersion: String by extra { "1.2.4" }
val kotlinVersion: String by extra { "1.3.50" }

buildscript {
    val kotlinVersion: String by extra { "1.3.50" }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm").version("1.3.21")
    application
    jacoco
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

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")

    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}

application {
    mainClassName = "entusiasme.AppKt"
}

val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.34.2")
}

val ktlintTask by tasks.creating(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("src/**/*.kt")
}

tasks.named("check").get().dependsOn(ktlintTask)

val ktlintFormat by tasks.creating(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Fix Kotlin code style deviations"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("-F", "src/**/*.kt")
}

val detekt by configurations.creating

dependencies {
    detekt("io.gitlab.arturbosch.detekt:detekt-formatting:1.0.0-RC15")
    detekt("io.gitlab.arturbosch.detekt:detekt-cli:1.0.0-RC15")
}

val detektTask by tasks.creating(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Runs a failfast detekt build."
    main = "io.gitlab.arturbosch.detekt.cli.Main"
    classpath = detekt

    val baseline = "$rootDir/resources/detekt/baseline.xml"
    val config = "$rootDir/resources/detekt/detekt.yml"
    val input = files("src/main/kotlin", "src/test/kotlin")
}

tasks.named("check").get().dependsOn(detektTask)

jacoco {
    toolVersion = "0.8.4"
    reportsDir = file("$buildDir/reports/jacoco")
    applyTo(tasks.run.get())
}

tasks.register<JacocoReport>("applicationCodeCoverageReport") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Runs Jacoco Coverage Report."
    executionData(tasks.run.get())
    sourceSets(sourceSets.main.get())
}
