![Java CI with Maven](https://github.com/link-intersystems/lis-gradle-maven-central/workflows/Java%20CI%20with%20Gradle/badge.svg)

This repository contains plugins that configure builds to deploy artefacts to maven central using the sonatype proxy.  

# Plugins

## com.link-intersystems.gradle.maven-central-project [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.maven-central-project/com.link-intersystems.gradle.maven-central-project.gradle.plugin)](https://mvnrepository.com/artifact/maven-central-project)

Add this plugin to the root project's `build.gradle.kts`

```kotlin
// build.gradle.kts
plugins {
    id("com.link-intersystems.gradle.maven-central-project") version "0.0.1"
}
```

## com.link-intersystems.gradle.maven-central-library [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.maven-central-library/com.link-intersystems.gradle.maven-central-library.gradle.plugin)](https://mvnrepository.com/artifact/maven-central-library)

Add this plugin to the project's `build.gradle.kts` that contains a java library to deploy.

```kotlin
// build.gradle.kts
plugins {
    id("com.link-intersystems.gradle.maven-central-library") version "0.0.1"
}
```

Add publishing information to generate a valid pom. Otherwise, sonatype deploy checks will fail when you try to
close the staging repository. E.g.

```kotlin
// build.gradle.kts
publishing {
    afterEvaluate {
        publications.withType<MavenPublication> {
            pom {
                name.set("Lis Gradle Maven Central")
                description.set("Plugins to deploy libraries to the maven central repository.")
                url.set("https://github.com/link-intersystems/lis-gradle-maven-central")
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
                    url.set("https://github.com/link-intersystems/lis-gradle-maven-central")
                    connection.set("scm:git:https://github.com/link-intersystems/lis-gradle-maven-central.git")
                    developerConnection.set("scm:git:https://github.com/link-intersystems/lis-gradle-maven-central.git")
                }
            }
        }
    }
}
```

# How to deploy to maven central

All artifacts that are deployed to maven central need to be signed. Thus, you must provide a signing key and passphrase.

To ease the deployment you can use the `gradle_gpg` script instead of the `gradlew`.

## gradlew_gpg

Either pass the signing key fingerprint as the first argument

```shell
./gradlew_gpg 1CEFE097A0DC0F8C2F92688 publish
```

or set the singing key fingerprint as an environment variable

```shell
export GPG_KEY_FINGERPRINT=1CEFE097A0DC0F8C2F92688

./gradlew_gpg publish
```
you can pass any argument that can be passed to `.gradlew` to the `gradlew_gpg` script.

When the script executes it will ask you for the passphrase

```shell
$ ./gradlew_gpg publish
Enter passphrase: **************

> Configure project :
Signing publications
<-------------> 3% EXECUTING [7s]
> :initializeSonatypeStagingRepository
```

