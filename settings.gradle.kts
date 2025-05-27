pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    plugins {
        id("com.google.devtools.ksp") version "2.1.21-2.0.1"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }

    versionCatalogs {
        create("libs") {
            version("kotlin", "2.1.21")
            version("nav", "2.7.7")
        }
    }
}

rootProject.name = "Playlist Maker"
include(":app")
 