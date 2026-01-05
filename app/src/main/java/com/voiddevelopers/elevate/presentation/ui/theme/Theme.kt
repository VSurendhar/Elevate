package com.voiddevelopers.elevate.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)

data class TextColor(
    val titleColor: Color, val subTitle1Color: Color,
    val subTitle2Color: Color,
    val descriptionColor: Color,
)

fun ColorScheme.textColors(): TextColor {
    return TextColor(
        titleColor = Color.Black,
        subTitle1Color = subTitle1Black,
        subTitle2Color = subTitle2Black,
        descriptionColor = descriptionGray,
    )
}

private val LightColorScheme = lightColorScheme(
    primary = aquaTeal,
    onPrimary = white,
    secondary = tealDark,
    onSecondary = white,
    tertiary = tealSoft,
    onTertiary = black,
    inversePrimary = lightGray,
    surface = whiteSmoke,
    onSurface = black,
    outline = outlineGray,
    surfaceVariant = white,
)

@Composable
fun ElevateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}