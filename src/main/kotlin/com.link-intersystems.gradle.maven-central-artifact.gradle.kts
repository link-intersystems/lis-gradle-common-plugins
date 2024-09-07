plugins {
    `maven-publish`
    signing
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
