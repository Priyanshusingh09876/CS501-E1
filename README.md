# Mobility Lens

CS501 E1 — Assignment 1  
Priyanshu Singh, U73441029

Single-screen Kotlin / Compose app. It shows one of the six mobile-vs-desktop dimensions at a time. Previous / Next move through them. Type an app name, hit Analyze, and you get either an error (empty field) or a short note that ties that name to the current dimension.

Package / applicationId: `com.priyanshu.mobilitylens`  
Emulator I used: Pixel XL, API 33  
minSdk 24, targetSdk 35, compileSdk 35

## How to run

1. Clone this repo.
2. In Android Studio Quail, File → Open and pick the cloned folder (the one that contains `app/` and `settings.gradle.kts`).
3. Let Gradle sync.
4. Run the `app` configuration on an emulator.

## What is in here

- `app/src/main/java/com/priyanshu/mobilitylens/MainActivity.kt` — UI and `remember` state
- `MobilityDimension.kt` — name / description / implication resource IDs
- `app/src/main/res/values/strings.xml` — all the copy, including the six dimensions
- `app/src/main/AndroidManifest.xml` — launcher activity, no extra permissions
- `app/build.gradle.kts` and `gradle/libs.versions.toml` — SDK and libraries
- `MobilityLens_Assignment1_Report.pdf` — writeup, screenshots, rotation notes

I did not commit `build/` or `local.properties`.

## Rotation

State is `remember`, not `rememberSaveable`. Go to dimension 3, type something, rotate: you land back on dimension 1 with an empty field. I left it that way; the assignment said we did not have to fix it.

## AI

I used Claude for project setup and build errors.
