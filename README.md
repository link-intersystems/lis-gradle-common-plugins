![Java CI with Maven](https://github.com/link-intersystems/lis-gradle-maven-central/workflows/Java%20CI%20with%20Gradle/badge.svg)

This repository contains plugins that configure builds to deploy artefacts to maven central using the sonatype proxy.  

# Plugins

## com.link-intersystems.gradle.maven-central-project [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.maven-central-project/com.link-intersystems.gradle.maven-central-project.gradle.plugin)](https://repo1.maven.org/maven2/com/link-intersystems/gradle/maven-central-project/com.link-intersystems.gradle.maven-central-project.gradle.plugin/)

Add this plugin to the root project's `build.gradle.kts`

```kotlin
// build.gradle.kts
plugins {
    id("com.link-intersystems.gradle.maven-central-project") version "+" // set a specific version
}
```

## com.link-intersystems.gradle.maven-central-artifact [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.maven-central-artifact/com.link-intersystems.gradle.maven-central-artifact.gradle.plugin)](https://repo1.maven.org/maven2/com/link-intersystems/gradle/maven-central-artifact/com.link-intersystems.gradle.maven-central-artifact.gradle.plugin/)

This the base plugin for the other more specific plugins listed below. It configures maven-publish and the signing plugin 

```kotlin
// build.gradle.kts
plugins {
    id("com.link-intersystems.gradle.maven-central-artifact") version "+" // set a specific version
}
```

The configured signing plugin expects that the following project properties are set:

| Project Property | Description                                               |
|------------------|-----------------------------------------------------------|
| signingKey       | The private key to sign the artifacts with in PEM format. |
| signingPassword  | The passphrase of the signing key.                        |

> [!TIP]
> You can set project properties via environment variables starting with ORG_GRADLE_PROJECT_
> 
> ORG_GRADLE_PROJECT_signingKey
> ORG_GRADLE_PROJECT_signingPassword

## com.link-intersystems.gradle.maven-central-library [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.maven-central-library/com.link-intersystems.gradle.maven-central-library.gradle.plugin)](https://repo1.maven.org/maven2/com/link-intersystems/gradle/maven-central-library/com.link-intersystems.gradle.maven-central-library.gradle.plugin/)

Add this plugin to the project's `build.gradle.kts` that contains a java library to deploy.

```kotlin
// build.gradle.kts
plugins {
    id("com.link-intersystems.gradle.maven-central-library") version "+" // set a specific version
}
```

## com.link-intersystems.gradle.maven-central-platform [![Maven Central Version](https://img.shields.io/maven-central/v/com.link-intersystems.gradle.maven-central-platform/com.link-intersystems.gradle.maven-central-platform.gradle.plugin)](https://repo1.maven.org/maven2/com/link-intersystems/gradle/maven-central-platform/com.link-intersystems.gradle.maven-central-platform.gradle.plugin/)

Add this plugin to the project's `build.gradle.kts` that contains a java platform (aka BOM) to deploy. See [Sharing dependency versions between projects](https://docs.gradle.org/current/userguide/platforms.html) for details.

```kotlin
// build.gradle.kts
plugins {
    id("com.link-intersystems.gradle.maven-central-platform") version "+" // set a specific version
}
```

## Maven Central Deployment Preconditions

1. **Complete Maven POM information**

    To deploy an artifact to the maven central repository via the sonatype staging repositories, you need to add more information
    to the project pom in order to pass pre-deployment checks.

    Add publishing information to generate a valid pom. Otherwise, sonatype deploy checks will fail when you try to
    close the staging repository. E.g.

    ```kotlin
    // build.gradle.kts
    publishing {
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
    ```
   
2. **Release Version**

   Ensure that the project artifacts version is a release version (without -SNAPSHOT), before you publish.
   Otherwise, the artifacts will be published to a sonatype snapshot repository and no staging repository is created.

# How to deploy to maven central

Ensure that all [preconditions](#maven-central-deployment-preconditions) are met.

All artifacts that are deployed to maven central need to be signed. Thus, you must provide a signingKey and signingPassword.
Take a look at [com.link-intersystems.gradle.maven-central-artifact](#comlink-intersystemsgradlemaven-central-artifact-) above.

To ease the deployment you can use the [`exportGpgSigning`](https://gist.github.com/renelink/6a12336b5282c94a69a598deddf295ab) bash function.

After the gpg key and password is set you can just run `./gradlew publish`

```shell
$ ./gradlew publish

> Configure project :
Signing publications
<-------------> 3% EXECUTING [7s]
> :initializeSonatypeStagingRepository
```

