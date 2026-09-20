package com.priyanshusingh.focusplanbuilder.ui.theme

import androidx.compose.ui.graphics.Color

// ---------- Core palette ----------
// A calm, grounded identity for a study/focus tool: steady indigo as the
// anchor color, a warm amber used sparingly for the one primary action,
// and a soft parchment background instead of stark white.

/** Steady indigo -- the app's anchor color. Calm, not energetic. */
val FocusIndigo = Color(0xFF3E5C76)
val FocusIndigoDark = Color(0xFF26374F)

/** Warm amber -- spent in exactly one place: the Create Plan button. */
val FocusAmber = Color(0xFFF2A65A)
val FocusAmberDark = Color(0xFFD98A3D)

/** Soft, warm neutral background -- not stark white, not cold grey. */
val FocusParchment = Color(0xFFF7F5F1)
val FocusInk = Color(0xFF1B2430)

/** Muted surface tones for grouped input areas (not a heavy card). */
val FocusSurfaceLight = Color(0xFFEFECE5)
val FocusSurfaceDark = Color(0xFF232B36)

// ---------- Session-category accents ----------
// Each duration category gets its own accent, used as a left-edge stripe
// on the result card rather than a generic colored badge -- the color
// itself communicates which kind of session this is.

/** Quick review (10-29 min): a brief, bright burst. */
val CategoryQuickReview = Color(0xFFDE8F5F)

/** Focused session (30-60 min): the steady brand indigo itself. */
val CategoryFocusedSession = FocusIndigo

/** Extended session (61+ min): deeper, denotes sustained effort. */
val CategoryExtendedSession = Color(0xFF5B4B6B)
