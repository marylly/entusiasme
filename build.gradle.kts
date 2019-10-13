import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

tasks.named("check").get().dependsOn(ktlintTask)
