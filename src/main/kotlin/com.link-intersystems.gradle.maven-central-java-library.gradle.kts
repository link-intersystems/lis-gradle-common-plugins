plugins {
    `java-library`
    id("com.link-intersystems.gradle.maven-central-java")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}




