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
}

rootProject.name = "Packtagram"
include(":app")
include(":feature:newsfeed")
include(":feature:stories")
include(":feature:search")
include(":feature:messaging")
include(":feature:profile")
include(":core")
