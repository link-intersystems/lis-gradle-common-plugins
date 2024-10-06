rootProject.name = "lis-gradle-common-plugins"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.link-intersystems.gradle.multi-module") version "0.5.4"
    id("com.link-intersystems.gradle.published-artifact") version "0.1.0" apply false
    id("com.link-intersystems.gradle.java-project") version "0.1.0" apply false
    id("com.link-intersystems.gradle.java-library") version "0.1.0" apply false
    id("com.link-intersystems.gradle.maven-central-project") version "0.1.0" apply false
    id("net.researchgate.release") version "3.0.2" apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}