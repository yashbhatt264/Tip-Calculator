package com.yb.tipcalculator.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SAPPHIRE_PRIMARY,
    secondary = SAPPHIRE_SECONDARY,
    tertiary = SURFACE_DARK,
    background = BACKGROUND_DARK_LOGIN,
    surface = DARK_BG_NEW,
    surfaceVariant = CARD_BG_DARK,
    onBackground = TEXT_DARK,
    onSurface = TEXT_DARK,
    onSurfaceVariant = SURFACE_VARIANT_DARK,
    onPrimary = TEXT_DARK_100,
    onSecondary = TEXT_DARK_60,
    error = ERROR_DARK
)

private val LightColorScheme = lightColorScheme(
    primary = SAPPHIRE_PRIMARY,
    secondary = SAPPHIRE_SECONDARY,
    tertiary = SURFACE_LIGHT,
    background = LIGHT_BG_VNT,
    surface = LIGHT_BG_VNT,
    surfaceVariant = CARD_BG_LIGHT,
    onBackground = TEXT_LIGHT,
    onSurface = TEXT_LIGHT,
    onSurfaceVariant = SURFACE_VARIANT_LIGHT,
    onPrimary = TEXT_DARK_100,
    onSecondary = TEXT_LIGHT_60,
    error = ERROR_LIGHT

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TipCalculatorTheme(
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
        typography = Typography,
        content = content
    )
}