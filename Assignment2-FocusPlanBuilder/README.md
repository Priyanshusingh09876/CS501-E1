# Focus Plan Builder

**Name:** Priyanshu Singh
**BU ID:** U73441029
**Assignment:** Individual Coding Assignment 2 — Focus Plan Builder
**Package:** `com.priyanshusingh.focusplanbuilder`

## Description

Focus Plan Builder is a single-screen Android application, built entirely
with Kotlin and Jetpack Compose (Material 3), that helps a student turn a
subject and an amount of available time into a short, structured study plan.
The user enters a subject and a number of minutes (10–180); the app
validates both fields, and once they're valid, generates a plan showing a
duration category ("Quick review", "Focused session", or "Extended
session"), a recommended break length, and a one-sentence summary.

## Running the app

1. Open the project root folder (`Focus_Plan_Builder/`) in Android Studio
   (Giraffe or newer recommended).
2. Let Gradle sync automatically (it will download the Compose BOM and
   related dependencies on first sync).
3. Select a phone emulator (or connect a physical device) running API 24+.
4. Click **Run ▶** to build and launch `MainActivity`.
5. To run the unit tests: `./gradlew testDebugUnitTest`
   To run the instrumented UI tests: `./gradlew connectedDebugAndroidTest`
   (requires a running emulator/device).

## Screenshot

_[INSERT A SCREENSHOT OF THE RUNNING APP HERE, e.g. `![Screenshot](screenshot.png)`]_

## State and recomposition explanation (~150–250 words)

`FocusPlanRoute` is the composable that owns all of this screen's state:
`subject`, `minutesText`, and the generated `plan` all live there as
`mutableStateOf` values, two of them wrapped in `rememberSaveable`.
`FocusPlanScreen` is stateless — it only receives these values as
parameters and reports user actions back up through callbacks
(`onSubjectChange`, `onMinutesChange`, `onCreatePlan`).

The text fields store their values as `String`, not `Int`, because a
`TextField`'s `value` must always be able to represent whatever the user has
currently typed — including empty text, a partial number, or invalid
characters like "abc" — states that don't fit into an `Int` at all. Trying
to store an `Int` would either crash or silently discard the user's
keystrokes.

`toIntOrNull()` is safer than `toInt()` because `toInt()` throws a
`NumberFormatException` on any non-numeric or malformed string, which would
crash the app the moment the user typed a letter or left the field blank.
`toIntOrNull()` instead returns `null`, which the app treats as "not a valid
number yet."

The button's `enabled` state is recomputed every recomposition from
`canCreatePlan = subject.isNotBlank() && minutes != null && minutes in
10..180`. Any state change that feeds into that expression — a keystroke in
either text field — triggers recomposition and re-evaluates it, so the
button updates automatically with no separate boolean to keep in sync.

`rememberSaveable` preserves its value across configuration changes (like
screen rotation) by saving it into the Activity's saved-instance-state
`Bundle`, not just in memory. A plain local variable (or plain `remember`)
would be lost entirely when the Activity is destroyed and recreated, since
Compose's in-memory composition is thrown away and rebuilt at that point.

## Visual design

The UI uses a deliberate identity rather than the default Compose template
purple:
- **Palette:** a steady indigo as the anchor color, a warm amber spent only
  on the Create Plan button, and a soft parchment background instead of
  stark white.
- **Category accents:** each duration category (Quick review / Focused
  session / Extended session) gets its own accent color, shown as a stripe
  on the left edge of the result card — the color itself communicates which
  kind of session it is, not just the text label.
- **Motion:** one deliberate animation — the result card fades and expands
  into view when a plan is created, and fades/shrinks away when the inputs
  change — rather than scattered hover/entrance effects everywhere.
- Dynamic color (Android 12+ wallpaper-based theming) is intentionally
  turned off so the chosen palette actually renders on real devices.

## AI use

_[Fill in per your course's AI-use policy before submitting. For example:]_

- **Tool used:** _[e.g. Claude, ChatGPT, GitHub Copilot, or "None"]_
- **What assistance it provided:** _[e.g. "Generated the initial Compose
  layout structure and the two calculation functions based on my
  specification; I did not use it for the README write-up answers, which I
  wrote myself."]_
- **What portions I changed or verified:** _[Be specific — which files,
  which functions, what you modified.]_
- **How I confirmed I understand the submitted code:** _[e.g. "I traced
  through each test case in the Required Testing table by hand against the
  `durationCategory` and `recommendedBreak` functions, and I can explain the
  state-hoisting pattern between `FocusPlanRoute` and `FocusPlanScreen` to a
  classmate."]_

You are responsible for being able to explain every line of this submission.
