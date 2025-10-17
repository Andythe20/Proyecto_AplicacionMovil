package com.example.appshop.navigation

import LoginScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appshop.ui.auth.AuthScreen
import com.example.appshop.ui.auth.SignupScreen
import com.example.appshop.ui.components.MainLayout
import com.example.appshop.ui.views.CreateProfileScreen
import com.example.appshop.ui.views.HomeScreen

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

        // --- Pantallas sin Drawer (auth/login/signup) ---
        composable("auth") { AuthScreen(modifier, navController) }
        composable(
            route = "login",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -1000 },
                    animationSpec = tween(500)
                )
            }
        ) { LoginScreen(modifier, navController) }
        composable(
            route = "signup",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -1000 },
                    animationSpec = tween(500)
                )
            }
        ) { SignupScreen(modifier, navController) }

        // --- Pantallas con Drawer lateral ---
        composable("home") {
            MainLayout(navController) { padding ->
                HomeScreen(modifier.padding(padding), navController)
            }
        }
        composable("createProfile") {
            MainLayout(navController, title = "Crear Perfil") { padding ->
                CreateProfileScreen(modifier = Modifier.padding(padding))
            }
        }
    }
}
