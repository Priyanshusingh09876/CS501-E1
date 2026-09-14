# Mobility Lens

CS501 E1 — Assignment 1  
Priyanshu Singh, U73441029

Single-screen Kotlin / Compose app. It shows one of the six mobile-vs-desktop dimensions at a time. Previous / Next move through them. Type an app name, hit Analyze, and you get either an error (empty field) or a short note that ties that name to the current dimension.

Package / applicationId: `com.priyanshu.mobilitylens`  
Source folder: `app/src/main/java/com/student/mobilitylens/`  
Emulator: Pixel XL, API 33  
minSdk 24, targetSdk 35, compileSdk 35

## How to run

1. Clone https://github.com/Priyanshusingh09876/CS501-E1
2. In Android Studio Quail, File → Open → **Assignment1-MobilityLens** (the folder with `app/` and `settings.gradle.kts`). Do not open the repo root.
3. Let Gradle sync.
4. Run `app`. I used Pixel XL, API 33.

## What is in this folder

- `app/` — MainActivity, MobilityLensApp, AppHeader, DimensionProgressBar, DimensionCard, NavigationButtons, AnalyzePanel, DimensionCatalog, MobilityDimension, theme
- `app/build.gradle.kts` and `gradle/libs.versions.toml` — SDK and libraries
- `MobilityLens_Report_FINAL.pdf` — writeup
- `MobilityLens_Disclosure_FINAL.pdf` — AI / collaboration disclosure
- `screenshots/` — emulator captures

`build/` and `local.properties` are not in the repo.

## Rotation

State is `remember`, not `rememberSaveable`. Go to dimension 3, type something, rotate: you land back on dimension 1 with an empty field. I left it that way; the assignment said we did not have to fix it.

## AI

I used Claude for project setup and build errors, and Cursor to check the assignment list and split composables. I wrote the six dimension descriptions myself.
