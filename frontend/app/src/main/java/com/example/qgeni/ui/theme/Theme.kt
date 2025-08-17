package com.example.qgeni.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = OldMan,
    primary = OldMan,
    secondary = AsianSkin,
    surfaceContainerHigh = OldMan,
    tertiary = SadCloud,
    onPrimary = Color.Black,
    onBackground = Color.White,
    surfaceContainerHighest = AsianSkin,
    tertiaryContainer = OceanEyes,
    error = Incorrect,
    onError = Correct
)

private val LightColorScheme = lightColorScheme(
    background = OldMan,
    primary = OldMan,
    secondary = AsianSkin,
    surfaceContainerHigh = BigAvocado,
    tertiary = SadCloud,
    onPrimary = Color.White,
    onBackground = Color.Black,
    surfaceContainerHighest = AsianSkin,
    tertiaryContainer = OceanEyes,
    error = IncorrectIntense,
    onError = CorrectIntense
)

@Composable
fun QGenITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}