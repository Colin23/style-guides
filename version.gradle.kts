// THIS DOESN'T WORK OUT OF THE BOX. I CURRENTLY HAVE THIS IN THE build.gradle.kts file

class ApplicationVersion(
    private val major: Int,
    private val minor: Int,
    private val patch: Int,
    private val release: Boolean
) {
    private fun getRelease(): String {
        return if (release) "" else "-SNAPSHOT"
    }

    fun getVersion(): String {
        return "$major.$minor.$patch" + getRelease()
    }
}

val loadVersion: () -> String = {
    val versionPropertiesFile = File("src/main/resources/version.properties")

    if (!versionPropertiesFile.exists()) {
        throw Exception("No version.properties file found")
    }

    val versionProperties = Properties()

    versionPropertiesFile.inputStream().use { stream ->
        versionProperties.load(stream)
    }

    val major = versionProperties.getProperty("major")?.toInt() ?: 0
    val minor = versionProperties.getProperty("minor")?.toInt() ?: 0
    val patch = versionProperties.getProperty("patch")?.toInt() ?: 0
    val release = versionProperties.getProperty("release")?.toBoolean() ?: false

    val version = ApplicationVersion(major, minor, patch, release)
    version.getVersion()
}

tasks.register("majorVersionUpdate") {
    group = "versioning"
    description = "Bump to next major version"

    doFirst {
        val versionFile = File("src/main/resources/version.properties")
        val properties = Properties()

        versionFile.inputStream().use { stream ->
            properties.load(stream)
        }

        properties.setProperty("major", (properties.getProperty("major").toInt() + 1).toString())
        properties.setProperty("minor", "0")
        properties.setProperty("patch", "0")

        versionFile.outputStream().use { properties.store(it, null) }
    }
}

tasks.register("minorVersionUpdate") {
    group = "versioning"
    description = "Bump to next minor version"

    doFirst {
        val versionFile = file("src/main/resources/version.properties")
        val properties = Properties()
        versionFile.inputStream().use { properties.load(it) }

        properties.setProperty("minor", (properties.getProperty("minor").toInt() + 1).toString())
        properties.setProperty("patch", "0")

        versionFile.outputStream().use { properties.store(it, null) }
    }
}

tasks.register("patchVersionUpdate") {
    group = "versioning"
    description = "Bump to next patch version"

    doFirst {
        val versionFile = file("src/main/resources/version.properties")
        val properties = Properties()
        versionFile.inputStream().use { properties.load(it) }

        properties.setProperty("patch", (properties.getProperty("patch").toInt() + 1).toString())

        versionFile.outputStream().use { properties.store(it, null) }
    }
}

tasks.register("releaseVersion") {
    group = "versioning"
    description = "Make the version a release"

    doFirst {
        val versionFile = file("src/main/resources/version.properties")
        val properties = Properties()
        versionFile.inputStream().use { properties.load(it) }

        properties.setProperty("release", "true")

        versionFile.outputStream().use { properties.store(it, null) }
    }
}

tasks.register("preReleaseVersion") {
    group = "versioning"
    description = "Make the version a pre release"

    doFirst {
        val versionFile = file("src/main/resources/version.properties")
        val properties = Properties()
        versionFile.inputStream().use { properties.load(it) }

        properties.setProperty("release", "false")

        versionFile.outputStream().use { properties.store(it, null) }
    }
}