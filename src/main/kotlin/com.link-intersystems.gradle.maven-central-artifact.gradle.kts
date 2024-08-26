plugins {
    `maven-publish`
    signing
}

afterEvaluate {
    signing {
        val signingKey: String? by project
        val signingKeyExists = signingKey != null

        val signingPassword: String? by project
        val signingPasswordExists = signingPassword != null

        logger.debug("signingKey exists = {}, signingPassword {}", signingKeyExists, signingPasswordExists)

        val signingEnabled = signingKey != null && signingPassword != null
        logger.debug("signingEnabled = {}", signingEnabled)

        isRequired = signingEnabled

        if (signingEnabled) {
            useInMemoryPgpKeys(signingKey, signingPassword)
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