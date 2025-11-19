package com.example.appshop.navigation

import LoginScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appshop.ui.auth.AuthScreen
import com.example.appshop.ui.auth.SignupScreen
import com.example.appshop.ui.components.MainLayout
import com.example.appshop.ui.views.HomeScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appshop.db.AppDatabase
import com.example.appshop.db.repository.UserRepository
import com.example.appshop.ui.SplashScreen
import com.example.appshop.ui.views.CartScreen
import com.example.appshop.ui.views.CreateProfileScreen
import com.example.appshop.ui.views.ProductListScreen
import com.example.appshop.ui.views.RecipeSearchScreen
import com.example.appshop.viewmodel.AuthViewModel
import com.example.appshop.viewmodel.AuthViewModelFactory
import com.example.appshop.viewmodel.ProductViewModel
import com.example.appshop.viewmodel.RecipesViewModel


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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // --- CREAMOS EL VIEWMODEL UNA SOLA VEZ AQUÍ ---
    val context = LocalContext.current
    // 'remember' es clave para que no se recree en cada redibujado
    val db = remember { AppDatabase.getDatabase(context) }
    val repo = remember { UserRepository(db.userDao()) }
    val viewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repo))
    val viewProductModel: ProductViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash", modifier = modifier) {

        composable("splash") {
            SplashScreen(navController = navController)
        }

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
        ) { LoginScreen(modifier, navController, viewModel) }
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
        ) { SignupScreen(modifier, navController, viewModel) }

        // --- Pantallas con Drawer lateral ---
        composable("home") {
            MainLayout(navController) { padding ->
                HomeScreen(modifier.padding(padding),
                    viewModel,
                    navController = navController)
            }
        }
        composable("createProfile") {
            MainLayout(navController) { padding ->
                CreateProfileScreen(
                    modifier = Modifier.padding(padding),
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }

        composable("products"){
            MainLayout(navController) { padding ->
                ProductListScreen(
                    modifier = Modifier.padding(padding),
                    viewModel = viewProductModel,
                    navController = navController
                )
            }
        }

        composable(route = "cart") {
            MainLayout(navController) { padding ->
                CartScreen()
            }
        }

        composable("recipeSearch") {
            MainLayout(navController) { padding ->
                RecipeSearchScreen()
            }
        }
    }
}