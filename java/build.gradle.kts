import de.thetaphi.forbiddenapis.gradle.CheckForbiddenApis

plugins {
    java // Use this for developing standalone Java applications
    // `java-library` Use this for developing libraries and APIs
    idea
    checkstyle
    // Use this for adding Spring Boot specific gradle tasks and/or centralizing versioning.
    // This also comes with the spring boot BOM, meaning if only Spring Boot dependencies are used, there's no need for adding the dependency-management plugin.
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7" // Adds possibility for BOM managed dependency versions
    id("io.freefair.lombok") version "8.13.1"
    id("de.thetaphi.forbiddenapis") version "3.9"
}

group = "com.colinmoerbe"
// Needed for naming the JAR file. Use 'SNAPSHOT' during implementation, once the feature is done, delete it.
// For deleting 'SNAPSHOT' make a new commit it tag it with the release version
version = "0.0.1-SNAPSHOT" // Get this from the custom versioning methods

springBoot { // Exposed additional information about the application to the /info actuator endpoint.
    buildInfo()
}

java {
    sourceCompatibility = JavaVersion.VERSION_23 // Gradle should use Java 23 features and Syntax when compiling
    toolchain {
        // Gradle checks for a local Java 23 version and uses it if one is found.
        // If there's no local version, the build crashes. The foojay-resolver-convention plugin is needed then.
        languageVersion.set(JavaLanguageVersion.of(23))
        vendor = JvmVendorSpec.ADOPTIUM // Gradle uses Eclipse Temurin (AdoptOpenJDK HotSpot)
    }
}

repositories {
    mavenCentral() // Use Maven Central for resolving dependencies.
}

dependencyManagement {
    imports {
        mavenBom("org.junit:junit-bom:5.12.2")
        mavenBom("com.google.guava:guava-bom:33.4.8-jre")
    }
}

val slf4jVersion = "2.0.17"
val jakartaVersion = "3.0.0"
val archunitVersion = "1.4.0"

dependencies {
    implementation("org.springframework:spring-context")
    implementation("com.google.guava:guava")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("jakarta.annotation:jakarta.annotation-api:$jakartaVersion")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // ArchitectureTests dependencies.
    testImplementation("com.tngtech.archunit:archunit:$archunitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Version"] = version
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<DefaultTask>("checkstyleMain").configure {
    isEnabled = true
}

tasks.named<DefaultTask>("checkstyleTest").configure {
    isEnabled = false
}

tasks.named<CheckForbiddenApis>("forbiddenApisMain").configure {
    bundledSignatures =
        setOf("jdk-unsafe", "jdk-deprecated", "jdk-internal", "jdk-non-portable", "jdk-system-out", "jdk-reflection")
    signaturesFiles = project.files("config/forbidden-apis.txt")
    isEnabled = true
    setExcludes(setOf("**/api/**/*.class")) // This has to reference the .class files in the build dir.
}

tasks.named<CheckForbiddenApis>("forbiddenApisTest").configure {
    bundledSignatures = setOf("jdk-unsafe", "jdk-deprecated", "jdk-internal", "jdk-non-portable", "jdk-reflection")
    signaturesFiles = project.files("config/forbidden-apis.txt")
    isEnabled = true
}

tasks.named("check").configure {
    dependsOn(tasks.named("forbiddenApisMain"))
}

// sourceSets["main"].java.srcDirs("src/main/gen") Mark generated directories as source directories.

idea {
    module {
        // generatedSourceDirs.add(project.file("src/main/gen")) Only needed when an additional source directory is needed.
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

checkstyle {
    configFile = project.file("config/checkstyle.xml")
    toolVersion = "10.23.0"
}
