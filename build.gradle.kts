plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.1"
}

group = "ru.florestdev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url="https://jitpack.io")
}

dependencies {
    implementation("com.github.felipeucelli:JavaTube:f249f5f")
}

tasks.test {
    useJUnitPlatform()
}