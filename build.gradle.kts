plugins {
    `kotlin-dsl`
    id("maven-publish") // if you never publish the plugin, you may remove this (but it also does not hurt)
    signing
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("net.researchgate.release") version "3.0.2"
}


dependencies {
    implementation("io.github.gradle-nexus:publish-plugin:2.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.mockito:mockito-core:5.11.0")
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }

    withSourcesJar()
    withJavadocJar()
}


nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
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


signing {
    val signingKey: String? by project
    val signingKeyExists = signingKey != null

    val signingPassword: String? by project
    val signingPasswordExists = signingPassword != null

    logger.debug("signingKey exists = {}, signingPassword {}", signingKeyExists, signingPasswordExists)

    val signingEnabled = signingKey != null && signingPassword != null
    logger.debug("signingEnabled = {}", signingEnabled)

    isRequired = signingEnabled

    sign(publishing.publications)

    if (signingEnabled) {
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
}


val pushToRemoteName = if (project.findProperty("pushToRemote") != null) "origin" else ""

release {
    tagTemplate = "v\${version}"

    git {
        pushToRemote = pushToRemoteName
    }
}