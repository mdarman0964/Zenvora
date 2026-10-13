package com.junkfood.seal.ui.theme

import android.os.Build
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDirection
import com.google.android.material.color.MaterialColors
import com.junkfood.seal.ui.common.LocalFixedColorRoles
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.dynamicColorScheme

fun Color.applyOpacity(enabled: Boolean): Color {
    return if (enabled) this else this.copy(alpha = 0.62f)
}

@Composable
@ReadOnlyComposable
fun Color.harmonizeWith(other: Color) =
    Color(MaterialColors.harmonize(this.toArgb(), other.toArgb()))

@Composable
@ReadOnlyComposable
fun Color.harmonizeWithPrimary(): Color =
    this.harmonizeWith(other = MaterialTheme.colorScheme.primary)

@Composable
fun SealTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrastModeEnabled: Boolean = false,
    content: @Composable () -> Unit,
) {
    val view = LocalView.current

    LaunchedEffect(darkTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (darkTheme) {
                view.windowInsetsController?.setSystemBarsAppearance(
                    0,
                    APPEARANCE_LIGHT_STATUS_BARS,
                )
            } else {
                view.windowInsetsController?.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_STATUS_BARS,
                    APPEARANCE_LIGHT_STATUS_BARS,
                )
            }
        }
    }

    // Zenvora brand palette: violet accents with a calm midnight surface in dark mode.
    val colorScheme =
        dynamicColorScheme(!darkTheme).run {
            val branded =
                if (darkTheme) {
                    copy(
                        primary = Color(0xFFB9A2FF),
                        onPrimary = Color(0xFF25105A),
                        primaryContainer = Color(0xFF382477),
                        onPrimaryContainer = Color(0xFFE8DDFF),
                        secondary = Color(0xFFB8C4FF),
                        tertiary = Color(0xFFFFB7D5),
                        background = Color(0xFF101019),
                        surface = Color(0xFF101019),
                        surfaceContainerLowest = Color(0xFF0B0B12),
                        surfaceContainerLow = Color(0xFF171722),
                        surfaceContainer = Color(0xFF1D1C29),
                        surfaceContainerHigh = Color(0xFF272536),
                        surfaceContainerHighest = Color(0xFF323044),
                    )
                } else {
                    copy(
                        primary = Color(0xFF6542C0),
                        onPrimary = Color.White,
                        primaryContainer = Color(0xFFE9DDFF),
                        onPrimaryContainer = Color(0xFF25105A),
                        secondary = Color(0xFF4C5DA8),
                        tertiary = Color(0xFF9A416C),
                    )
                }
            if (isHighContrastModeEnabled && darkTheme)
                branded.copy(
                    surface = Color.Black,
                    background = Color.Black,
                    surfaceContainerLowest = Color.Black,
                    surfaceContainerLow = Color.Black,
                    surfaceContainer = Color.Black,
                    surfaceContainerHigh = Color.Black,
                    surfaceContainerHighest = Color.Black,
                )
            else branded
        }

    val textStyle =
        LocalTextStyle.current.copy(
            lineBreak = LineBreak.Paragraph,
            textDirection = TextDirection.Content,
        )

    val tonalPalettes = LocalTonalPalettes.current

    CompositionLocalProvider(
        LocalFixedColorRoles provides FixedColorRoles.fromTonalPalettes(tonalPalettes),
        LocalTextStyle provides textStyle,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content,
        )
    }
}

@Composable
@Deprecated("Use SealTheme instead", replaceWith = ReplaceWith("SealTheme(content)"))
fun PreviewThemeLight(content: @Composable () -> Unit) {
    SealTheme(darkTheme = false, content = content)
}
