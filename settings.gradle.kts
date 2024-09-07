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
    id("com.link-intersystems.gradle.maven-central-artifact") version "0.0.10" apply false
    id("com.link-intersystems.gradle.maven-central-project") version "0.0.10" apply false
    id("com.link-intersystems.gradle.maven-central-java") version "0.0.10" apply false
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}