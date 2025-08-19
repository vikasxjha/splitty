package com.splitwise.easy.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PrimaryGreen = Color(0xFF2E7D32)
private val PrimaryDarkGreen = Color(0xFF1B5E20)
private val AccentOrange = Color(0xFFFF6B35)
private val AccentDarkOrange = Color(0xFFE55100)
private val BackgroundLight = Color(0xFFF5F5F5)
private val BackgroundWhite = Color(0xFFFFFFFF)
private val ErrorRed = Color(0xFFD32F2F)
private val TextPrimary = Color(0xFF212121)
private val TextSecondary = Color(0xFF757575)
private val StatusPaid = Color(0xFF4CAF50)
private val StatusPending = Color(0xFFFFC107)
private val StatusOverdue = Color(0xFFF44336)

@Composable
fun SplittyEasyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PrimaryGreen,
            onPrimary = BackgroundWhite,
            secondary = AccentOrange,
            onSecondary = BackgroundWhite,
            background = BackgroundLight,
            onBackground = TextPrimary,
            error = ErrorRed,
            onError = BackgroundWhite,
            surface = BackgroundWhite,
            onSurface = TextPrimary
        ),
        typography = Typography(),
        content = content
    )
}

