package com.example.appshop.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appshop.R
import com.example.appshop.ui.theme.PacificoFontFamily
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController){
    LaunchedEffect(key1 = true) {
        delay(4200)
        navController.navigate("auth") { // Navega y configura la acción en un solo paso
            // Le decimos que, como parte de esta navegación,
            // elimine la ruta "splash" del historial.
            popUpTo("splash") { inclusive = true }
        }
    }

    Splash()
}

@Composable
fun Splash(){
    // Creamos el estado para la animación del alpha (transparencia).
    // 'remember' es clave para que el estado sobreviva a las recomposiciones.
    val alphaAnim = remember { Animatable(0f) }

    // Usamos LaunchedEffect para ejecutar la animación UNA SOLA VEZ.
    // 'key1 = true' asegura que se lanza solo cuando el Composable entra en la composición.
    LaunchedEffect(key1 = true) {
        delay(800)

        // Animamos el valor de alpha desde 0f (invisible) hasta 1f (totalmente visible)
        // en 1500 milisegundos (1.5 segundos).
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200)
        )

        delay(1000)

        alphaAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 1200)
        )

    }

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.onlyflans_logo),
            modifier = Modifier
                .height(250.dp)
                .clip(RoundedCornerShape(50.dp)),
            contentScale = ContentScale.Crop,
            contentDescription = "Logo OnlyFlans"
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "versión para móviles",
            fontSize = 30.sp,
            fontFamily = PacificoFontFamily,
            fontWeight = FontWeight.Medium,
            // Aplicamos el valor animado del alpha al Modifier del Text.
            // A medida que `alphaAnim.value` cambia de 0 a 1, el texto se desvanece hacia adentro.
            modifier = Modifier.alpha(alphaAnim.value)
        )
    }
}
