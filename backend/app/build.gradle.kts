val JUNIT_VERSION               = "5.11.+"

val JACKSON_CORE_VERSION        = "2.18.0"
val JACKSON_ANNOTATIONS_VERSION = "2.18.0"
val JACKSON_DATABIND_VERSION    = "2.18.0"

val MOCKITO_VERSION             = "5.10.+"
val MOCKITO_JUNIT_VERSION       = "5.10.+"

plugins {
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

    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$JACKSON_DATABIND_VERSION")

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

tasks.jar {
    manifest {
        attributes["Main-Class"] = "ch.zhaw.studyflow.Main"
    }
}

tasks.distTar {
    enabled=false
}

tasks.register<Copy>("distribute") {
    group = "distribution"
    description = "Distributes the app and its dependencies into a directory"

    val outputDir = layout.buildDirectory.dir("distributions/app")
    from(configurations.runtimeClasspath) {
        into("deps")
    }
    from(tasks.named("jar")) {
        into("libs")
    }
    into(outputDir)
}