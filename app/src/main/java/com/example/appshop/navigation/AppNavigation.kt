package com.example.appshop.navigation

import LoginScreen
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
 * Utiliza un `NavHost` para definir un grafo de navegación, que es un mapa de todas las
 * pantallas (destinos) y las rutas que llevan a ellas.
 *
 * Esta es la implementación del patrón "Single-Activity Architecture", donde una única
 * Activity (`MainActivity`) alberga todos los fragmentos o pantallas (`Composables`).
 *
 * @param modifier Modificador para el `NavHost`.
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    // `rememberNavController` crea y recuerda un NavController.
    // Este controlador es el cerebro de la navegación; se usa para cambiar de pantalla (`navigate`).
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth", modifier = modifier) {
        composable("auth") { AuthScreen(modifier, navController) }
        composable("login") { LoginScreen(modifier) }
        composable("signup") { SignupScreen( modifier, navController)}
    }
}
