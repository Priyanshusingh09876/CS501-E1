package com.priyanshu.mobilitylens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DimensionProgressBar(
    currentIndex: Int,
    total: Int,
    accentColor: Color
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(total) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            if (index <= currentIndex) accentColor
                            else Color(0xFFDDE1F0)
                        )
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = "${stringResource(R.string.dimension_label)} ${currentIndex + 1} ${stringResource(R.string.of_label)} $total",
            fontSize = 12.sp,
            color = Color(0xFF90A4AE),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}