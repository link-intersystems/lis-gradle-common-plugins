import gradle.kotlin.dsl.accessors._273c41c017a30de2a6ca8df633927332.publish

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



subprojects {
    val closeAndRelease: String? by this
    val closeAndReleaseEnabled = closeAndRelease ?: "true"

    if (closeAndReleaseEnabled == "true") {
        // closeAndReleaseStagingRepositories if maven-publish is applied
        pluginManager.withPlugin("org.gradle.maven-publish") {
            tasks.publish {
                finalizedBy(tasks.closeAndReleaseStagingRepositories)
            }
        }
    }
}

