pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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
        create("versions") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "Packtflix"
include(":app")
include(":feature:list")
include(":feature:playback")
include(":feature:login")
include(":common")
