# CS501 E1 — Mobile Application Development

Priyanshu Singh  
BU ID: U73441029

Course work for CS501 E1. Homework, research, and the final project each live in their own folder in this repo.

To run something, clone the repo and in Android Studio use **File → Open** on that folder (the one with `app/` and `settings.gradle.kts`). Do not open this root folder.

## Assignments

| Folder | Assignment | Summary |
|---|---|---|
| [Assignment1-MobilityLens](Assignment1-MobilityLens/) | Individual Coding Assignment 1 | Single-screen Kotlin / Compose app that walks through the six mobile-vs-desktop dimensions one at a time with Previous / Next navigation. Type an app name and tap Analyze to get a short note tying that app to the current dimension, or an error for an empty field. Composables split into header, progress bar, dimension card, navigation, and analyze panel. State uses `remember`, so it intentionally resets on rotation. Includes the written report and AI disclosure PDFs. |
| [Assignment2-FocusPlanBuilder](Assignment2-FocusPlanBuilder/) | Individual Coding Assignment 2 | Single-screen Jetpack Compose (Material 3) app that validates a subject and a 10–180 minute duration, then builds a study plan with a duration category, recommended break, and summary sentence. Demonstrates state hoisting (`FocusPlanRoute` / `FocusPlanScreen`), `rememberSaveable` across rotation, null-safe parsing with `toIntOrNull()`, and a pure-Kotlin model layer. 116 JUnit unit tests and 22 Compose UI tests. |

Each folder has its own README with setup steps, screenshots, and the write-up for that assignment.

## Common setup

- Android Studio Ladybug or newer, with the bundled JDK 17
- Emulator: Pixel XL / API 33 was used for both assignments; any API 24+ device works
- minSdk 24, targetSdk 35, compileSdk 35 for both projects
- `build/` folders and `local.properties` are not committed
