package com.momentum.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Momentum dark color scheme — sage green primary, calm dark surfaces.
 * Missed/incomplete states use neutral gray, never alarming red.
 */
private val MomentumDarkColorScheme = darkColorScheme(
    primary = SageGreen,
    onPrimary = Color(0xFF0D2117),
    primaryContainer = SageGreenContainer,
    onPrimaryContainer = SageGreenLight,

    secondary = SteelBlue,
    onSecondary = Color(0xFF111C22),
    secondaryContainer = Color(0xFF1E2D36),
    onSecondaryContainer = SteelBlueLight,

    tertiary = WarmSand,
    onTertiary = Color(0xFF1F1C12),
    tertiaryContainer = Color(0xFF322F1F),
    onTertiaryContainer = WarmSandLight,

    error = SoftError,
    onError = Color(0xFF2A0A0A),
    errorContainer = SoftErrorContainer,
    onErrorContainer = Color(0xFFE8AAAA),

    background = Background,
    onBackground = OnBackground,

    surface = SurfaceContainer,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainerHigh,
    onSurfaceVariant = OnSurfaceMuted,

    outline = Color(0xFF4A5055),
    outlineVariant = Color(0xFF2F3336),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE2E2E5),
    inverseOnSurface = Color(0xFF2F3133),
    inversePrimary = SageGreenDark,
    surfaceTint = SageGreen,
)

@Composable
fun MomentumTheme(
    // Momentum is always dark — the product philosophy calls for a calm dark aesthetic
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MomentumDarkColorScheme,
        typography = MomentumTypography,
        content = content
    )
}
