package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = AthleticBlue,
    secondary = AthleticGreen,
    tertiary = AthleticCoral,
    background = DarkSlateBg,
    surface = DarkSlateSurface,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = LightSlateText,
    onSurface = LightSlateText,
    surfaceVariant = DarkSlateCard,
    onSurfaceVariant = LightSlateText
)

private val LightColorScheme = lightColorScheme(
    primary = AthleticBlue,
    secondary = AthleticGreen,
    tertiary = AthleticCoral,
    background = Color(0xFF050810), // Keep dark background for fitness premium vibe
    surface = DarkSlateSurface,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = LightSlateText,
    onSurface = LightSlateText,
    surfaceVariant = DarkSlateCard,
    onSurfaceVariant = LightSlateText
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme() || true, // Default to true for fitness vibe
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}

// Custom Glassmorphism Modifiers
fun Modifier.meshGradientBackground(): Modifier = this.drawBehind {
    // 1. Deep space solid color
    drawRect(Color(0xFF050810))
    
    // 2. Cyan blob top-left (e.g. at -10% left, -10% top, 50% width, 40% height)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x3322D3EE), Color.Transparent),
            center = Offset(-size.width * 0.1f, -size.height * 0.1f),
            radius = size.width * 0.8f
        ),
        radius = size.width * 0.8f,
        center = Offset(-size.width * 0.1f, -size.height * 0.1f)
    )
    
    // 3. Indigo blob bottom-right (e.g. at 110% right, 110% bottom, 60% width, 50% height)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x2B6366F1), Color.Transparent),
            center = Offset(size.width * 1.1f, size.height * 1.1f),
            radius = size.width * 0.9f
        ),
        radius = size.width * 0.9f,
        center = Offset(size.width * 1.1f, size.height * 1.1f)
    )
}

fun Modifier.frostedGlassCard(
    backgroundColor: Color = GlassCardBg,
    borderColor: Color = GlassCardBorder,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(16.dp)
): Modifier = this
    .background(backgroundColor, shape)
    .border(1.dp, borderColor, shape)

fun Modifier.frostedGlassHero(
    startColor: Color = GlassHeroBgStart,
    endColor: Color = GlassHeroBgEnd,
    borderColor: Color = GlassHeroBorder,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(24.dp)
): Modifier = this
    .background(
        Brush.linearGradient(
            colors = listOf(startColor, endColor)
        ),
        shape
    )
    .border(1.dp, borderColor, shape)
