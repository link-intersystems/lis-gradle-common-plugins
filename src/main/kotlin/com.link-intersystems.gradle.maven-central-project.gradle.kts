plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}

allprojects {
    val closeAndRelease: String? by this
    val closeAndReleaseEnabled = closeAndRelease ?: "true"

    if (closeAndReleaseEnabled == "true") {
        tasks.findByName("afterPublish")?.dependsOn("closeAndReleaseRepository")
    }
}

