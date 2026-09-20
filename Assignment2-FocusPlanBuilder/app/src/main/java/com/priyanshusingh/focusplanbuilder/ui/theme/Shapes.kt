package com.priyanshusingh.focusplanbuilder.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * The one corner-radius token shared by the text fields and the button on
 * this screen: a deliberate, grounded 14dp rounding -- not Compose's
 * default 4dp, and not a full pill shape. Defined once here so every
 * component that uses it stays in sync automatically.
 */
val FocusFieldShape = RoundedCornerShape(14.dp)
