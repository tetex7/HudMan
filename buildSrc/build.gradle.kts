
plugins {
    `java-library`
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("com.google.code.gson:gson:2.11.0")
}

java {
    val targetJavaVersion = 17
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}
