package com.voiddevelopers.elevate.util

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Dp.toPx(context: Context): Int = (this.value * context.resources.displayMetrics.density).toInt()

fun Modifier.applyBoxShadow(): Modifier {
    return this.dropShadow(
        shape = RoundedCornerShape(16.dp), shadow = Shadow(
            radius = 4.dp, color = Color.Gray, spread = 2.dp, alpha = 0.1f
        )
    )
}

@Composable
fun CustomRoundedCorderBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .applyBoxShadow()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        content()
    }
}