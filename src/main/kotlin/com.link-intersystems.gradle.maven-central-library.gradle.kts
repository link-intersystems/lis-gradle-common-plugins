plugins {
    `java-library`
    id("maven-publish") // if you never publish the plugin, you may remove this (but it also does not hurt)
    signing
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }

    withSourcesJar()
    withJavadocJar()
}

tasks.withType<ProcessResources> {
    filesMatching("**/*") {
        expand(project.properties)
    }
}

tasks.test {
    useJUnitPlatform()
}


signing {
    val signingKey = providers.environmentVariable("GPG_SIGNING_KEY")
    val signingPassphrase = providers.environmentVariable("GPG_SIGNING_PASSPHRASE")
    if (signingKey.isPresent) {
        useInMemoryPgpKeys(signingKey.get(), signingPassphrase.orNull)
        sign(publishing.publications)
        logger.lifecycle("Signing publications")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}









