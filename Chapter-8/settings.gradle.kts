pluginManagement {
    repositories {
        google( {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        })
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://mvnrepository.com/artifact/androidx.media3/media3-ui")
        }
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
