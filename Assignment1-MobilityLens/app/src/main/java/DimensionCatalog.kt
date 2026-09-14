package com.priyanshu.mobilitylens

import androidx.compose.ui.graphics.Color

fun buildDimensions(): List<MobilityDimension> = listOf(
    MobilityDimension(R.string.dim1_name, R.string.dim1_desc, R.string.dim1_impl),
    MobilityDimension(R.string.dim2_name, R.string.dim2_desc, R.string.dim2_impl),
    MobilityDimension(R.string.dim3_name, R.string.dim3_desc, R.string.dim3_impl),
    MobilityDimension(R.string.dim4_name, R.string.dim4_desc, R.string.dim4_impl),
    MobilityDimension(R.string.dim5_name, R.string.dim5_desc, R.string.dim5_impl),
    MobilityDimension(R.string.dim6_name, R.string.dim6_desc, R.string.dim6_impl)
)

val dimensionEmojis = listOf("👆", "📱", "⚡", "📍", "⏱️", "🔒")

val dimensionColors = listOf(
    Color(0xFF6C63FF),
    Color(0xFF00BCD4),
    Color(0xFFFF6B6B),
    Color(0xFF4CAF50),
    Color(0xFFFF9800),
    Color(0xFF9C27B0)
)