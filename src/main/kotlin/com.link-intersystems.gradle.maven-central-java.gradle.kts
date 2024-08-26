plugins {
    `java-library`
    id("com.link-intersystems.gradle.maven-central-artifact")
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



