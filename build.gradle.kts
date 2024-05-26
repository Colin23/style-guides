import de.thetaphi.forbiddenapis.gradle.CheckForbiddenApis

plugins {
    java // Use this for developing standalone Java applications
    `java-library` // Use this for developing libraries and APIs
    idea
    checkstyle
    // Use this for adding Spring Boot specific gradle tasks and/or centralizing versioning.
    // This also comes with the spring boot BOM, meaning if only Spring Boot dependencies are used, there's no need for adding the dependency-management plugin.
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5" // Adds possibility for BOM managed dependency versions
    id("io.freefair.lombok") version "8.6"
    id("de.thetaphi.forbiddenapis") version "3.7"
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

repositories {
    mavenCentral() // Use Maven Central for resolving dependencies.
}

dependencyManagement {
    imports {
        mavenBom ("org.springframework.boot:spring-boot-dependencies:3.3.0")
        mavenBom ("org.springframework:spring-framework-bom:6.1.8")
        mavenBom ("org.junit:junit-bom:5.10.2")
        mavenBom ("com.google.guava:guava-bom:33.2.0-jre")
    }
}

val archunitVersion = "1.3.0"
val slf4jVersion = "2.0.13"
val jakartaVersion = "3.0.0"

dependencies {
    implementation("org.springframework:spring-context")                // The dependency for Spring
    implementation("com.google.guava:guava")                            // The dependency for ImmutableLists
    implementation("org.slf4j:slf4j-api:$slf4jVersion")                 // The dependency for logging
    implementation("jakarta.annotation:jakarta.annotation-api:$jakartaVersion")   // The dependency for Jakarta EE annotations (@PostConstruct)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher") // Use JUnit Jupiter for testing.

    // ArchitectureTests dependencies.
    testImplementation("com.tngtech.archunit:archunit:$archunitVersion"){
        exclude(group = "org.slf4j") // Not necessarily needed. If It's deleted, the slf4j dependency can also be deleted
    }
    testImplementation("org.junit.jupiter:junit-jupiter") // Use JUnit Jupiter for testing.
}

// Only needed for listeners
tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Version"] = version
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
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

// Checkstyle tests for the test directory
tasks.named<DefaultTask>("checkstyleTest").configure {
    isEnabled = false
}

tasks.named("check").configure {
    dependsOn(tasks.named("forbiddenApisMain"))
}

sourceSets["main"].java.srcDirs("src/main/gen") // Mark generated directories as source directories.

idea {
    module {
        generatedSourceDirs.add(project.file("src/main/gen")) // Only needed when an additional source directory is needed.
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

checkstyle {
    configFile = project.file("checkstyle.xml")
    toolVersion = "10.12.4"
}
