package com.epico.calculadoradepropina.ui.theme

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

private val LightColors = lightColorScheme(
    primary = Color(0xFF1B6B4A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFA7F2C8),
    onPrimaryContainer = Color(0xFF002114),
    secondaryContainer = Color(0xFFD3E8DA),
    onSecondaryContainer = Color(0xFF0E1F16)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8CD5AD),
    onPrimary = Color(0xFF003824),
    primaryContainer = Color(0xFF005138),
    onPrimaryContainer = Color(0xFFA7F2C8),
    secondaryContainer = Color(0xFF384B3F),
    onSecondaryContainer = Color(0xFFD3E8DA)
)

@Composable
fun CalculadoraDePropinaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}