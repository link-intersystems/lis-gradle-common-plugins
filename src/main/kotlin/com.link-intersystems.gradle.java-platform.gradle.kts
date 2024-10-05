plugins {
    `java-platform`
    id("com.link-intersystems.gradle.published-artifact")
}

publishing {
    publications {
        create<MavenPublication>("javaPlatform") {
            from(components["javaPlatform"])
        }
    }
}