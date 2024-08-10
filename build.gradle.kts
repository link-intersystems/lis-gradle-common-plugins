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

afterEvaluate {
    publishing {
        publications.withType<MavenPublication> {
            pom {
                name.set("Lis Gradle Maven Central")
                description.set("Plugins to deploy libraries to the maven central repository.")
                url.set("https://github.com/link-intersystems/lis-gradle-maven-central")
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
                    url.set("https://github.com/link-intersystems/lis-gradle-maven-central")
                    connection.set("scm:git:https://github.com/link-intersystems/lis-gradle-maven-central.git")
                    developerConnection.set("scm:git:https://github.com/link-intersystems/lis-gradle-maven-central.git")
                }
            }
        }
    }

    signing {
        val signingKey = providers.environmentVariable("GPG_SIGNING_KEY")
        val signingPassphrase = providers.environmentVariable("GPG_SIGNING_PASSPHRASE")
        val signingEnabled = signingKey.isPresent && signingPassphrase.isPresent

        if (signingEnabled) {
            useInMemoryPgpKeys(signingKey.get(), signingPassphrase.orNull)
            sign(publishing.publications)

            val publishedGAVs = publishing.publications.withType(MavenPublication::class).flatMap {
                val groupId = it.groupId
                val artifactId = it.artifactId
                val artifactVersion = it.version

                val pomGav = "$groupId:$artifactId:pom:$artifactVersion"
                val allArtifactGAVs = mutableSetOf(pomGav)

                if (it.artifacts.isNotEmpty()) {
                    val artifactGAVs = it.artifacts.filter { a -> a.extension != null }.map {
                        val extension = it.extension
                        "$groupId:$artifactId:$extension:$artifactVersion"
                    }
                    allArtifactGAVs.addAll(artifactGAVs)
                }

                allArtifactGAVs
            }.joinToString(separator = "\n  - ", prefix = "  - ")

            logger.lifecycle("Signing publications: \n$publishedGAVs")
        } else {
            logger.info("Signing disabled. Set the GPG_SIGNING_KEY and GPG_SIGNING_PASSPHRASE environment variable to enable.")
        }
    }
}



release {
    tagTemplate = "v\${version}"
}