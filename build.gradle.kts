// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.10.0" apply false
    id("org.jetbrains.kotlin.android") version "2.1.21" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
    id("com.google.devtools.ksp") version "2.1.21-2.0.1" apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        val kotlin_version = "2.1.21"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}