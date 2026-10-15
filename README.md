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
| [Assignment3-CampusEventExplorer](Assignment3-CampusEventExplorer/) | Individual Coding Assignment 3 | Two-screen Compose app for browsing 12 campus events. The list screen has a "Search events" field that filters by title or category; the result is derived from a `rememberSaveable` query, so it survives rotation and the trip to the detail screen and back. Uses type-safe Navigation Compose (`@Serializable EventList` / `EventDetail(eventId)`, `toRoute()`), passing only the event ID. The detail screen has an Up button and a Share button that opens the system share sheet with an implicit `ACTION_SEND` intent. Invalid IDs show "Event unavailable" instead of crashing. Screens never receive a `NavController`. 63 JVM tests (unit + Robolectric Compose UI) plus a device rotation test. |

Each folder has its own README with setup steps, screenshots, and the write-up for that assignment.

## Common setup

- Android Studio Ladybug or newer, with JDK 17 (the bundled JDK works)
- Emulator: Pixel XL / API 33 was used for development; any API 24+ device works
- minSdk 24 for all projects
- Assignments 1 and 2: compileSdk / targetSdk 35
- Assignment 3: compileSdk / targetSdk 36 (install SDK Platform 36 in the SDK Manager if Gradle sync asks for it)
- `build/` folders and `local.properties` are not committed
