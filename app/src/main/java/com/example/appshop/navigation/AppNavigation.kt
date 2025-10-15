package com.example.appshop.navigation

import LoginScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appshop.ui.auth.AuthScreen
import com.example.appshop.ui.auth.SignupScreen

/**
 * Composable principal que gestiona la navegación de la aplicación.
 *
 * Utiliza un `NavHost` para definir un grafico de navegación, que es un mapa de todas las
 * pantallas (destinos) y las rutas que llevan a ellas.
 *
 * Esta es la implementación del patrón "Single-Activity Architecture", donde una única
 * Activity (`MainActivity`) alberga todos los fragmentos o pantallas (`Composables`).
 *
 * @param modifier Modificador para el `NavHost`.
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth", modifier = modifier) {

        composable("auth") { AuthScreen(modifier, navController) }

        // --- ANIMACIÓN PARA LA PANTALLA DE LOGIN ---
        composable(
            route = "login",
            // Animación para cuando esta pantalla ENTRA en escena
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500))
            },
            // Animación para cuando esta pantalla SALE de escena
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500))
            }
        ) {
            LoginScreen(modifier)
        }

        // --- ANIMACIÓN PARA LA PANTALLA DE SIGNUP ---
        composable(
            route = "signup",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500))
            }
        ) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
            )
        }
    }
}
