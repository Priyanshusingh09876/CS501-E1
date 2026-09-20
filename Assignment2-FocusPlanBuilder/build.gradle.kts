// Top-level build file. Module-specific configuration lives in app/build.gradle.kts.
// Plugin versions are declared once in gradle/libs.versions.toml (Gradle version catalog).
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
