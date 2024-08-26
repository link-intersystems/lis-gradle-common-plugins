plugins {
    `maven-publish`
    signing
}

afterEvaluate {
    signing {
        val signingKey = providers.environmentVariable("GPG_SIGNING_KEY")
        val signingPassphrase = providers.environmentVariable("GPG_SIGNING_PASSPHRASE")
        val signingEnabled = signingKey.isPresent && signingPassphrase.isPresent


        isRequired = signingEnabled

        sign(publishing.publications)

        if (signingEnabled) {
            useInMemoryPgpKeys(signingKey.get(), signingPassphrase.get())

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





