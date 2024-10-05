plugins {
    `kotlin-dsl`
    id("com.link-intersystems.gradle.published-artifact")
    id("com.link-intersystems.gradle.maven-central-project")
    id("com.link-intersystems.gradle.java-library")
    id("net.researchgate.release")
}


dependencies {
    implementation("io.github.gradle-nexus:publish-plugin:2.0.0")
    compileOnly("net.researchgate:gradle-release:3.0.2")
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