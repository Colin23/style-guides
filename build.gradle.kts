import de.thetaphi.forbiddenapis.gradle.CheckForbiddenApis

plugins {
    java // Use this for developing standalone Java applications
    `java-library` // Use this for developing libraries and APIs
    id("io.freefair.lombok") version "8.3"
    id("de.thetaphi.forbiddenapis") version "3.6"
    checkstyle
    idea
}

group = "com.colinmoerbe"
// Needed for naming the JAR file. Use 'SNAPSHOT' during implementation, once the feature is done, delete it.
// For deleting 'SNAPSHOT' make a new commit it tag it with the release version
version = "0.0.1-SNAPSHOT"

// Apply a specific Java toolchain to ease working on different environments. Always chose the latest LTS version.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

sourceSets["main"].java.srcDirs("src/main/gen") // Mark generated directories as source directories.

idea {
    module {
        generatedSourceDirs.add(project.file("src/main/gen")) // Only needed when an additional source directory is needed.
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

repositories {
    mavenCentral() // Use Maven Central for resolving dependencies.
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // ArchitectureTests dependencies.
    testImplementation("com.tngtech.archunit:archunit:1.2.0"){
        exclude(group = "org.slf4j") // Necessary to ignore the built-in slf4j-api:2.0.9 dependency
    }

    implementation("com.google.guava:guava:32.1.2-jre")             // The dependency for ImmutableLists
    implementation("org.springframework:spring-context:5.3.22")     // The dependency for Spring
    implementation("org.slf4j:slf4j-api:1.7.32")                    // The dependency for logging
    implementation("javax.annotation:javax.annotation-api:1.3.2")   // The dependency for Jakarta EE annotations (@PostConstruct)
}

// Generic imported ForbiddenApis and custom self-written ones.
tasks.named<CheckForbiddenApis>("forbiddenApisMain").configure {
    bundledSignatures = setOf("jdk-unsafe", "jdk-deprecated", "jdk-internal", "jdk-non-portable", "jdk-system-out", "jdk-reflection")
    signaturesFiles = project.files("forbidden-apis.txt")
    setExcludes(setOf("**/api/**/*.class")) // This has to reference the .class files in the build dir.
}

// Forbidden API tests for the 'test' directory
tasks.named<CheckForbiddenApis>("forbiddenApisTest").configure {
    bundledSignatures = setOf("jdk-unsafe", "jdk-deprecated", "jdk-internal", "jdk-non-portable", "jdk-reflection")
    signaturesFiles = project.files("forbidden-apis.txt")
    isEnabled = true
}

// Ignore the test directory
tasks.named<DefaultTask>("checkstyleTest").configure {
    isEnabled = false
}

tasks.named("check").configure {
    dependsOn(tasks.named("forbiddenApisMain"))
}

checkstyle {
    configFile = project.file("checkstyle.xml")
    toolVersion = "10.12.4"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Only needed for listeners
tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Version"] = version
    }
}
