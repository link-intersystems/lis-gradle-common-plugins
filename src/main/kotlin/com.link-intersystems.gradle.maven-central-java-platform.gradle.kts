plugins {
    `java-platform`
    id("com.link-intersystems.gradle.maven-central-artifact")
}

publishing {
    publications {
        create<MavenPublication>("javaPlatform") {
            from(components["javaPlatform"])
        }
    }
}