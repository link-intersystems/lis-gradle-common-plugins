plugins {
    id("com.link-intersystems.gradle.published-artifact")
    id("com.link-intersystems.gradle.java-project")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}




