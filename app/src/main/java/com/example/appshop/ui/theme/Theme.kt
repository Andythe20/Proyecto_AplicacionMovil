package com.example.appshop.ui.theme

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

private val DarkColorScheme =
    darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80,
    )

private val DarkColorPalette =
    darkColorScheme(
        primary = MutedPink, // Acento principal, más suave que el SoftPink.
        secondary = LightBrown, // Acento secundario que contrasta bien.
        background = DarkChocolate, // Fondo principal, oscuro y cálido.
        surface = DarkSurface, // Superficies (tarjetas) un poco más claras.
        onPrimary = DarkChocolate, // Texto oscuro sobre el acento primario claro.
        onSecondary = DarkChocolate, // Texto oscuro sobre el acento secundario claro.
        onBackground = OffWhite, // Texto claro (casi blanco) sobre el fondo oscuro.
        onSurface = OffWhite, // Texto claro sobre las superficies.
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40,
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

// paleta de colores personalizada
private val LightColorPalette =
    lightColorScheme(
        primary = Chocolate, // Color principal para botones y elementos importantes
        secondary = SoftPink, // Color secundario para otros elementos
        background = CreamPastel, // Color de fondo de las pantallas
        surface = OffWhite, // Color para superficies como tarjetas (Cards)
        onPrimary = Color.White, // Color del texto sobre el color primario (ej. texto en un botón Chocolate)
        onSecondary = TextPrimary, // Color del texto sobre el color secundario
        onBackground = TextPrimary, // Color del texto sobre el fondo principal
        onSurface = TextPrimary, // Color del texto sobre las superficies (tarjetas)
    )

@Composable
fun AppShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivar Dynamic color para visualizar colores personalizados.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorPalette
            else -> LightColorPalette
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
