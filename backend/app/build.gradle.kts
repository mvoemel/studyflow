val JUNIT_VERSION               = "5.11.+"

val JACKSON_CORE_VERSION        = "2.18.0"
val JACKSON_ANNOTATIONS_VERSION = "2.18.0"
val JACKSON_DATABIND_VERSION    = "2.18.0"

val MOCKITO_VERSION             = "5.10.+"
val MOCKITO_JUNIT_VERSION       = "5.10.+"

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:$JUNIT_VERSION")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.mockito:mockito-core:$MOCKITO_VERSION")
    testImplementation("org.mockito:mockito-junit-jupiter:$MOCKITO_JUNIT_VERSION")

    implementation("com.fasterxml.jackson.core:jackson-core:$JACKSON_CORE_VERSION")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$JACKSON_ANNOTATIONS_VERSION")
    implementation("com.fasterxml.jackson.core:jackson-databind:$JACKSON_DATABIND_VERSION")
}

application {
    mainClass = "ch.zhaw.studyflow.Main"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
