package com.priyanshusingh.focusplanbuilder.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Corner radius shared by both text fields and the primary button. */
val FieldShape = RoundedCornerShape(14.dp)

/** Corner radius for the input panel and the result card. */
val PanelShape = RoundedCornerShape(24.dp)

/** Small pill used for badges and chips. */
val PillShape = RoundedCornerShape(999.dp)

/**
 * Material shape scale for the theme. Components that read
 * `MaterialTheme.shapes` (chips, menus, etc.) pick these up automatically.
 */
val FocusShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = FieldShape,
    large = RoundedCornerShape(20.dp),
    extraLarge = PanelShape
)
