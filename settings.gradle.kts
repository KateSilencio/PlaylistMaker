pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            //version("kotlin", "1.9.22")
            version("kotlin", "2.1.20")
            version("nav", "2.7.7")
        }
    }
}

rootProject.name = "Playlist Maker"
include(":app")
 