plugins {
    `kotlin-dsl`
    id("com.link-intersystems.gradle.published-artifact")
    id("com.link-intersystems.gradle.java-project")
    id("com.link-intersystems.gradle.maven-central-project")
    id("net.researchgate.release") version "3.0.2"
}


dependencies {
    implementation("io.github.gradle-nexus:publish-plugin:2.0.0")
}


publishing.publications.withType<MavenPublication> {
    pom {
        name.set("Lis Gradle Common Plugins")
        description.set("Plugins to deploy libraries to the maven central repository.")
        url.set("https://github.com/link-intersystems/lis-gradle-common-plugins")
        licenses {
            license {
                name.set("Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("rene.link")
                name.set("René Link")
                email.set("rene.link@link-intersystems.com")
                organization.set("Link Intersystems GmbH")
                organizationUrl.set("https://www.link-intersystems.com")
                url.set("https://stackoverflow.com/users/974186/ren%C3%A9-link")
                roles.set(listOf("developer"))
            }
        }
        scm {
            url.set("https://github.com/link-intersystems/lis-gradle-common-plugins")
            connection.set("scm:git:https://github.com/link-intersystems/lis-gradle-common-plugins.git")
            developerConnection.set("scm:git:https://github.com/link-intersystems/lis-gradle-common-plugins.git")
        }
    }
}


publishing.repositories {
    maven {
        name = "TempLocal"
        url = uri(project.layout.buildDirectory.file(".m2/repository"))
    }
}

if (tasks.findByName("closeAndReleaseStagingRepositories") != null) {
    tasks.findByName("afterPublish")?.dependsOn("closeAndReleaseStagingRepositories")
}


// Work around for Cannot include build 'build-logic' in build '???'. This is not supported yet.
// See https://github.com/researchgate/gradle-release/issues/304
val realRelease = tasks.create<DefaultTask>("realRelease") {
    dependsOn(
        "createScmAdapter",
        "initScmAdapter",
        "checkCommitNeeded",
        "checkUpdateNeeded",
        "checkoutMergeToReleaseBranch",
        "unSnapshotVersion",
        "confirmReleaseVersion",
        "checkSnapshotDependencies",
        "runBuildTasks",
        "preTagCommit",
        "createReleaseTag",
        "checkoutMergeFromReleaseBranch",
        "updateVersion",
        "commitNewVersion"
    )
}


configure(listOf(tasks.release, tasks.runBuildTasks)) {
    configure {
        onlyIf { false }
    }
}


val pushToRemoteName = if (project.findProperty("pushToRemote") != null) "origin" else ""

release {
    tagTemplate = "v\${version}"

    git {
        pushToRemote = pushToRemoteName
    }
}