val logback_version: String by project
val exposed_version: String by project

plugins {
    kotlin("jvm") version "1.3.70"
}

group = "io.commands4k"
version = "3.0"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("com.discord4j:discord4j-core:3.0.12")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
